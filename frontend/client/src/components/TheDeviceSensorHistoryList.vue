<template>
<v-card class="d-flex flex-column pa-4">
    <div class="search">
        <v-autocomplete
            ref="searchInput"
            v-model="selectedAutocompleteDevice"
            variant="plain"
            clearable
            :label="searchFieldPlaceholderText"
            :items="searchInputData"
            menu-icon=""
            return-object
            :clear-icon="ClearIconComponent" />
    </div>
    <v-tabs v-model="selectedTab" fixed-tabs class="flex-0-0">
        <v-tab class="fs-5">
            {{ tabContent }}
        </v-tab>
    </v-tabs>
    <v-card :class="$style['device-list-card']">
        <v-progress-linear v-if="loading" class="mt-1" />
        <v-expansion-panels v-model="expandedGroup" class="h-100">
            <v-expansion-panel v-for="sensorName in sensorDevices" :key="sensorName" :value="sensorName" :title="sensorName" :class="$style.panel">
                <template #text>
                    <v-progress-linear v-if="getMeasurmentTypesBySensor.fetching.value && expandedGroup === sensorName" />
                    <v-radio-group v-model="selectedDeviceFromGroup" column hide-details :class="extractCssClass('device-list', $style)">
                        <v-radio v-for="(measurementTypeValue, index) in measurementTypesForSensor(sensorName)" :key="index" 
                                 :value="`${measurementTypeValue}|${findSelectedSensorId()}`" :label="measurementTypeValue" color="primary" />
                    </v-radio-group>
                    <div v-if="measurementTypesForSensor(sensorName).length === 0 && !getMeasurmentTypesBySensor.fetching.value" style="display:flex; align-items: center; height: 50px; margin-top: -20px;margin-left: 20px">No measurements</div>
                </template>
            </v-expansion-panel>
        </v-expansion-panels>
    </v-card>
</v-card>
</template>

<script lang="ts">
export type SelectedDeviceAndGroup = {
    device?: DisplayableDevice,
    group?: MeasurementType
}
</script>

<script setup lang="ts">
import { ref, computed, watch, watchEffect } from "vue"
import { h } from "vue"


import type { DisplayableDevice, SearchItem } from "@/graphql/types"
import { MeasurementType, type GetMeasurmentTypesBySensorQuery, type GetMeasurmentTypesBySensorQueryVariables, GetMeasurmentTypesBySensorDocument } from "@/generated/graphql"

import SvgIcon, { IconType } from "@/components/SvgIcon.vue"

import _ from "lodash"
import { extractCssClass } from "@/utils"
import { useRoute } from "vue-router"
import { useQuery } from "@urql/vue"
import { useUserStore } from "@/stores/UserStore"
import { useDeviceGroupsInfo } from "@/composables/useDeviceGroups"

type Device = {
    id: string;
    name: string | null;
};

type DeviceArray = Device[] | null | undefined;

const props = withDefaults(defineProps<{
    modelValue?: SelectedDeviceAndGroup
    devices: Array<DisplayableDevice>
    loading: boolean
    deviceNames: DeviceArray
}>(), {
    modelValue: () => ({
        device: undefined,
        group: undefined,
    }),
})
const emit = defineEmits<{
    (e: "update:modelValue", modelValue: SelectedDeviceAndGroup): void
    (e: "groupSelected", group: MeasurementType): void
}>()
const route = useRoute()
const { accountId } = useUserStore()
const { selectedDeviceGroupInfo } = useDeviceGroupsInfo()

const selectedTabFromNotif = ref(route.params.selectedTab)
const deviceIdFromNotif: any = ref(route.params.id)
const measurementTypeFromNotif = route.params.measurementType
let measurementType: MeasurementType
let selectDeviceAuto: any

if (measurementTypeFromNotif === "AIR_PRESSURE"){            
    measurementType = MeasurementType.AIR_PRESSURE
} else if (measurementTypeFromNotif === "TEMPERATURE") {
    measurementType = MeasurementType.TEMPERATURE
} else if (measurementTypeFromNotif === "HUMIDITY") {
    measurementType = MeasurementType.HUMIDITY
} else if (measurementTypeFromNotif === "RSRP") {
    measurementType = MeasurementType.RSRP
} else if (measurementTypeFromNotif === "BATTERY_VOLTAGE") {
    measurementType = MeasurementType.BATTERY_VOLTAGE
}

const selectedDeviceFromGroup = computed({
    get: () => `${props.modelValue.group}|${props.modelValue.device?.id}`,
    set: (deviceGroupAndDeviceId: string) => {
        const tokens = deviceGroupAndDeviceId.split("|")
        emit("update:modelValue", {
            device: props.devices.find((device: DisplayableDevice) => device.id === tokens[1]),
            group: tokens[0] as MeasurementType,
        })
    },
})

const expandedGroup = ref<string>()
const selectedTab = ref<"SENSOR">()
watchEffect(() => {
    if (selectedTab.value === "SENSOR") {    
        waitForDevice();
    }
})

function waitForDevice() {
    if (props.devices.length === 0) {
        return;
    }
    const device = props.devices.find(device => device.id === deviceIdFromNotif.value);
    if (device) {
        expandedGroup.value = device.name;
        selectDeviceAuto = `${measurementType}|${deviceIdFromNotif.value}`;
        selectedDeviceFromGroup.value = selectDeviceAuto;
    }
    selectedTabFromNotif.value = "";
}
const findSelectedSensorId = () => {
    let selectedSensorId = null;
    props.devices.forEach((device: DisplayableDevice) => {
        if (expandedGroup.value === device.name) {
            selectedSensorId = device.id;
        }
    });

    return selectedSensorId;
};

const getMeasurmentTypesBySensor = useQuery<GetMeasurmentTypesBySensorQuery, GetMeasurmentTypesBySensorQueryVariables>({
    query: GetMeasurmentTypesBySensorDocument,
    variables: computed(() => ({
        accountId: accountId!,
        deviceGroupId: selectedDeviceGroupInfo.value?.id ?? "",
        deviceId: findSelectedSensorId(),
    })),
})
const measurementTypesForSensor = (sensorName: string) => {
    const measurementTypes: MeasurementType[] = [];
    const device = getMeasurmentTypesBySensor.data.value?.account.deviceGroup?.device;

    if (device) {
        if (device.name === sensorName && device.measurements) {
            for (const measurement of device.measurements) {
                if (!measurementTypes.includes(measurement.type)) {
                    measurementTypes.push(measurement.type);
                }
            }
        }
    }
    return measurementTypes;
};

const sensorDevices = computed(() => {
    const sensorNames = new Set<string>();
    
    props.devices.forEach((device: DisplayableDevice) => {           
        sensorNames.add(device.name ?? '');
    });

    return Array.from(sensorNames);
});
const selectedAutocompleteDevice = ref<{title: string, value: string}>()
watch(selectedAutocompleteDevice, (formFieldValue => {
    const device = props.devices.find(device => device.id === formFieldValue?.value);
    if (device) {
        expandedGroup.value = device.name;
    }
}))

const tabContent = "Sensors"

const searchFieldPlaceholderText = computed(() => "Sensor History")
const searchInputData = computed<Array<SearchItem>>(() => {
    const devicesOfType = props.devices
    return devicesOfType.map((device: DisplayableDevice) => ({
        title: device.name,
        value: device.id,
    }))
})
const searchInput = ref<HTMLInputElement>()

const ClearIconComponent = () => h(SvgIcon, {
    type: IconType.FONT_AWESOME,
    category: "fas",
    name: "xmark",
    alt: "Clear input",
    class: [ "icon-color--black clear-input-btn" ],
    role: "button",
})
</script>

<style lang="scss" module>
@import "../assets/styles/functions";

.device-list-card {
    flex: 0 1 auto;
    overflow-y: hidden;

    .device-list {
        max-height: 100%;
        overflow-y: auto;
        padding: 16px 4px;
        scrollbar-width: thin;

        :global {
            ::-webkit-scrollbar {
                width: 2px;
            }
        }

        *::-webkit-scrollbar-thumb {
            display: none;
        }
    }

    .panel {
        display: flex;
        flex-direction: column;
        max-height: 100%;

        :global {
            .v-expansion-panel-title {
                min-height: unset;
                padding: 16px 24px;
                font-size: 1rem;
            }

            .v-expansion-panel-text {
                flex: 0 1 auto;
                overflow-y: auto;

                &__wrapper {
                    padding: 0;
                }
            }
        }
    }
}
</style>
