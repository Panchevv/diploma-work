<template>
<div class="w-100 h-100">
    <v-progress-linear v-if="deviceMeasurementHistoryQueryResult.fetching.value" />
    <div>
        <Line :data="chartData" :options="chartOptions" :height="chartHeight" :width="chartWidth" />
    </div>
</div>
</template>

<script setup lang="ts">
import { computed, reactive, ref, watch } from "vue"

import { useTheme } from "vuetify"
import { useRoute } from "vue-router"
import { useQuery, useSubscription } from "@urql/vue"

import { useSubscriptionToken } from "@/composables/useSubscriptionToken"
import { useUserStore } from "@/stores/UserStore"
import { useDeviceGroupsInfo } from "@/composables/useDeviceGroups"

import type { ChartData, ChartDataset, ChartOptions } from "chart.js"
import type { Maybe } from "graphql/jsutils/Maybe"
import type { DisplayableDevice } from "@/graphql/types"
import { MeasurementType, GetDeviceMeasurementHistoryDocument, SubscribeDeviceMeasurementsDocument } from "@/generated/graphql"
import type { Measurement, InputMaybe, GetDeviceMeasurementHistoryQuery, GetDeviceMeasurementHistoryQueryVariables, SubscribeDeviceMeasurementsSubscription, SubscribeDeviceMeasurementsSubscriptionVariables } from "@/generated/graphql"

import { Line } from "vue-chartjs"
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, TimeScale, PointElement, LineElement } from "chart.js"
import  "chartjs-adapter-date-fns"

import _ from "lodash"
import { computedWithPrevious } from "@/utils"
import { units } from "@/utils"

const props = defineProps<{
    device: DisplayableDevice
    measurementType: MeasurementType
}>()

const chartHeight = computed(() => {
    return window.innerWidth < 600 ? 320 : 0; 
})

const chartWidth = computed(() => {
    return window.innerWidth < 600 ? 200 : 0; 
})

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, TimeScale, PointElement, LineElement)

const theme = useTheme()
const route = useRoute()

const { accountId } = useUserStore()

const chartDataColors = {
    [MeasurementType.TEMPERATURE]: theme.current.value.colors.error,
    [MeasurementType.HUMIDITY]: theme.current.value.colors.warning,
    [MeasurementType.AIR_PRESSURE]: theme.current.value.colors.info,
    [MeasurementType.BATTERY_VOLTAGE]: "orange",
    [MeasurementType.RSRP]: "black",
}

const switchedDeviceGroups = ref(false)
const { selectedDeviceGroupInfo } = useDeviceGroupsInfo()
watch(selectedDeviceGroupInfo, () => {
    switchedDeviceGroups.value = true
})

const currentDate = ref(new Date())
const from = computed(() => route.query.from ?
    new Date(route.query.from as string) :
    new Date((new Date(currentDate.value)).setDate(currentDate.value.getDate() - 2)))
const to = computed(() => route.query.to ? new Date(route.query.to as string) : currentDate.value)
// trigger history refetch on group change
watch(selectedDeviceGroupInfo, () => {
    currentDate.value = new Date()
    Object.assign(lastMeasurementEndCursor, {})
})

const deviceId = computed(() => props.device.id)
const measurementType = computed(() => props.measurementType)

const lastMeasurementEndCursor = reactive<Record<string, {[k in MeasurementType]: InputMaybe<string>}>>({})
// Gets current measurement too
const deviceMeasurementHistoryQueryResult = useQuery<GetDeviceMeasurementHistoryQuery, GetDeviceMeasurementHistoryQueryVariables>({
    query: GetDeviceMeasurementHistoryDocument,
    variables: {
        accountId: accountId!,
        //@ts-ignore
        deviceId,
        //@ts-ignore
        measurementType,
        from: computed(() => from.value.toISOString()),
        to: computed(() => to.value.toISOString()),
        first: 500,
        // @ts-ignore
        after: computed(() => lastMeasurementEndCursor[deviceId.value]?.[measurementType.value]),
    },
})
/*
    * Workaround for a bug with BE returning `hasNextPage` = true when no more pages are available.
    * `startCursor` remains the `startCursor` of the first page when normalized caching is enabled so `startCursor` === `endCursor` can't be used
    * to check if last page is received
    * `deviceMeasurementHistoryQueryResult.fetching` becomes false and then immediately true after each page is fetched and remains false after the last page is fetched
    */
const fetchedAll = ref(false)
const setFetchedAll = _.debounce((fetching: boolean) => {
    fetchedAll.value = fetching
}, 1000)
watch(deviceMeasurementHistoryQueryResult.fetching, (fetching) => {
    setFetchedAll(!fetching)
})

const clearPreviousResult = ref(false)
watch(() => props.measurementType, () => {
    clearPreviousResult.value = true
})

// Includes current measurement too
const deviceMeasurementHistory = computed(() => {
    const currentMeasurement = deviceMeasurementHistoryQueryResult.data.value?.account.device?.measurement as Measurement
    const measurementHistory =  deviceMeasurementHistoryQueryResult.data.value?.account.device?.measurement?.history
    const measurements: Array<Measurement> = measurementHistory?.edges?.
        map(measurementEdge => measurementEdge.node) ?? []

    return currentMeasurement ? [ ...measurements, currentMeasurement ] : measurements
})
watch(deviceMeasurementHistory, () => {
    clearPreviousResult.value = false
    const measurementHistory = deviceMeasurementHistoryQueryResult.data.value?.account.device?.measurement?.history
    if (measurementHistory != null && !fetchedAll.value) {
        if (!(deviceId.value in lastMeasurementEndCursor))
            lastMeasurementEndCursor[deviceId.value] = {} as {[k in MeasurementType]: InputMaybe<string>}
        lastMeasurementEndCursor[deviceId.value][measurementType.value] = measurementHistory.pageInfo.endCursor
    }
})

const subscriptionToken = useSubscriptionToken()
const deviceMeasurementSubscription = useSubscription<SubscribeDeviceMeasurementsSubscription, SubscribeDeviceMeasurementsSubscription, SubscribeDeviceMeasurementsSubscriptionVariables>({
    query: SubscribeDeviceMeasurementsDocument,
    variables: computed(() => ({
        token: subscriptionToken.value!,
    })),
    pause: computed(() => subscriptionToken.value === undefined),
})

const liveMeasurements = computedWithPrevious<SubscribeDeviceMeasurementsSubscription['measurements']>((previous) => {
    const measurements = deviceMeasurementSubscription.data.value?.measurements ?? []
    if (previous === undefined)
        return measurements
    return previous.concat(measurements)
}, computed(() => switchedDeviceGroups.value))
watch(liveMeasurements, () => {
    switchedDeviceGroups.value = false
})

const chartData = computed<ChartData<"line">>(() => {
    if (clearPreviousResult.value) {
        return {
            labels: [] as Array<string>,
            datasets: [ {
                normalized: true,
                label: `${MeasurementType[props.measurementType]}`,
                data: [],
                backgroundColor: chartDataColors[props.measurementType],
            } ] as Array<ChartDataset<"line">>,
        } as ChartData<"line">
    }

    const historyPlusLiveMeasurements = deviceMeasurementHistory.value.concat(liveMeasurements.value.filter((measurement) => measurement.type === props.measurementType) ?? [])
    const uniqueMeasurements = [ ...historyPlusLiveMeasurements.reduce((accumulator: Map<string, Measurement>, current: Measurement) => {
        accumulator.set(current.id, current)
        return accumulator
    }, new Map<string, Measurement>).values() ]

    const measurements: Array<Measurement> = uniqueMeasurements
    const measurementsTimes: Array<Date> = _.uniq(measurements.map((measurement: Measurement) => measurement.when)).map((when: string) => new Date(when))
    const measurementsByDate: { [key: number]: Measurement } = measurements.
        sort((measurement: Measurement) => new Date(measurement.when).valueOf()).
        reduce((accumulator: { [key: number]: Measurement }, measurement: Measurement) => {
            accumulator[(new Date(measurement.when)).valueOf()] = measurement
            return accumulator
        }, {})

    const labels = measurementsTimes

    const data: ChartData<"line"> = measurements.length > 0 ? {
        labels,
        datasets: [ {
            normalized: false,
            label: `${MeasurementType[props.measurementType]}`,
            // taken by order. if 2 dates first point will go to date 1 and second to date 2. if only 1 point of set it'll go to date1
            data: measurementsTimes.map((date: Date) => {
                if (date.valueOf() in measurementsByDate)
                    return measurementsByDate[date.valueOf()]
                return undefined
            }).map((measurement: Maybe<Measurement>) => measurement?.value) as Array<number>,
            backgroundColor: chartDataColors[MeasurementType.AIR_PRESSURE],
        } ],
    } : {
        labels: [] as Array<string>,
        datasets: [] as Array<ChartDataset<"line">>,
    } as ChartData<"line">
    return data
})
const chartOptions = computed<ChartOptions>(() => ({
    responsive: true,
    plugins: {
        legend: {
            labels: {
                usePointStyle: true,
            },
            position: "bottom",
            align: "center",
        },
        tooltip: {
            callbacks: {
                label: (item) =>
                    `${item.dataset.label}: ${item.formattedValue} ${units[props.measurementType]}`,
            },
        },
    },
    scales: {
        y: {
            ticks: {
                callback: function(value) {
                    return value
                },
            },
        },
    },
}))
</script>

<style lang="scss" module>

</style>
