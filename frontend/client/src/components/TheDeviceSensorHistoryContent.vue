<template>
<v-card class="d-flex flex-column pa-4">
    <v-tabs v-model="selectedTab" style="min-width: 200px;" show-arrows fixed-tabs class="flex-0-1">
        <v-tab v-for="(tab, idx) in tabContent" :key="idx" :value="idx" size="small" density="compact" :height="40" :disabled="tab.disabled"
               class="fs-4">
            <SvgIcon :type="IconType.MATERIAL" :category="tab.icon.category" :name="tab.icon.name" :alt="tab.icon.alt"
                     class="mr-2" :class="selectedTab === idx ? 'icon-color--primary' : undefined" />
            {{ tab.name }}
        </v-tab>
    </v-tabs>
    <template v-if="selectedDevice != null">
        <TheDeviceSensorHistoryContentMetricHistory v-if="selectedGroup !== undefined" v-cshow.important="selectedTab === 'metricHistory'" :device="selectedDevice" :measurement-type="selectedGroup" />
    </template>
    <template v-else>
        <div style="padding: 16px; border-radius: 10px;" class="v-alert v-theme--light bg-error v-alert--density-default v-alert--variant-flat flex-0-1 mt-3">
            Please, select a device from the list.
        </div>
    </template>
</v-card>
</template>

<script setup lang="ts">
import { ref } from "vue"
import { computed } from "vue"

import { MeasurementType } from "@/generated/graphql"
import type { DisplayableDevice } from "@/graphql/types"

import SvgIcon, { IconType } from "@/components/SvgIcon.vue"
import TheDeviceSensorHistoryContentMetricHistory  from "@/components/TheDeviceSensorHistoryContentMetricHistory.vue"

import { useRoute } from "vue-router"
const props = defineProps<{
    selectedGroup?: MeasurementType
}>()

const route = useRoute();

const selectedDevice = defineModel<DisplayableDevice>()

const tabContent = computed(() => ({
    metricHistory: {
        name: "Metric History",
        icon: {
            type: IconType.MATERIAL,
            category: "filled",
            name: "history",
            alt: "History icon",
        },
        disabled: selectedDevice.value === undefined || selectedDevice?.value?.measurements?.length === 0,
    },
}))

const selectedTab = ref<string>("metricHistory")
</script>

<style lang="scss" module>
.scrollable-tabs {
    min-width: 560px;
}
</style>
