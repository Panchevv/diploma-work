<template>
<div :class="pickCssClass('searchbar-buttons', $style)">
    <div style="width: 300px;">
        <v-text-field v-model="filter" variant="underlined" placeholder="Search device" hide-details>
            <SvgIcon :type="IconType.MATERIAL" category="round" name="search" alt="magnifying_glass" />
        </v-text-field>
    </div>
    <TheExpansionGroupPanelActions v-model="selectedDevice" :company-data="filteredCompanyData" />
</div>

<FixedWidthScrollableTable width="100%" body-height="400px" :class="extractCssClass('group-table', $style)">
    <template #head>
        <tr>
            <colgroup> <col /> <col /> <col /> <col /> <col /> </colgroup>
            <th></th>
            <th></th>
            <th>Name</th>
            <th>Device ID</th>
        </tr>
    </template>
    <template #body>
        <tr v-for="device in filteredCompanyData" :key="device.id">
            <colgroup> <col /> <col /> <col /> <col /> <col /> </colgroup>
            <td>
                <v-radio-group v-model="selectedDeviceId" hide-details>
                    <v-radio :value="device.id" />
                </v-radio-group>
            </td>
            <td>{{ device?.name }}</td>
            <td>{{ device?.id }}</td>
        </tr>
    </template>
</FixedWidthScrollableTable>
</template>

<script setup lang="ts">
import { ref, computed, watch } from "vue"

import type { Device } from "@/generated/graphql"

import FixedWidthScrollableTable from "@/components/FixedWidthScrollableTable.vue"
import SvgIcon, { IconType } from "@/components/SvgIcon.vue"
import TheExpansionGroupPanelActions from "@/components/TheExpansionGroupPanelActions.vue"

import { extractCssClass, pickCssClass } from "@/utils"

type GroupInfo = {
    devices: Array<Device>
}
const props = defineProps<GroupInfo>()
defineEmits<{ click: any }>() // v-expansion-panel expects it's children to have onClick event and gives warning otherwise
const filter = ref("")
const selectedDevice = ref<Device>()
const selectedDeviceId = ref<string>()

watch(selectedDeviceId, (selectedDeviceId) => selectedDevice.value = props.devices.find((device: Device) => device.id === selectedDeviceId)!)
watch(() => props.devices, () => selectedDevice.value = undefined)

const filteredCompanyData = computed<Array<Device>>(() => {
    const filteredText = filter.value.toLowerCase()
    return props.devices.filter((device: Device) => device?.name?.toLowerCase().includes(filteredText))
})
</script>

<style lang="scss" module>
.searchbar-buttons {
    position: sticky;
    background-color: white;
    top: 0;
    z-index: 403;
    padding: 2rem 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    @media only screen and (max-width: 800px) {
        flex-direction: column;
        align-items: flex-start;
        padding: 0;
    }

}
.group-table {
    box-shadow: 0 2px 4px -1px rgba(0,0,0,.2), 0 4px 5px 0 rgba(0,0,0,.14), 0 1px 10px 0 rgba(0,0,0,.12);
    @media only screen and (max-width: 950px) {
        width: 230vw;
    }
    :global {
        thead {
            height: 4rem;
            background-color: rgba(var(--v-theme-secondary), .75);
            color: rgb(var(--v-theme-on-secondary));
            text-align: left;
            vertical-align: center;
            @media only screen and (max-width: 800px) {
                overflow: scroll;
            }

            tr {
                height: 100%;
            }
            th:first-child {
                border-radius: 5px 0 0 0;
            }
            th:last-child {
                border-radius: 0 5px 0 0;
            }
        }
        tbody {
            td:not(:first-child) {
                color: rgba(var(--v-theme-on-surface), .75);
                border-bottom: thin solid rgba(var(--v-border-color), var(--v-border-opacity));
                height: 60px;
            }
        }
        col {
            &:nth-child(1) {
                width: 5%;
            }

            &:nth-child(2) {
                width: 30%;
            }

            &:nth-child(3) {
                width: 35%;
            }

            &:nth-child(4) {
                width: 10%;
            }
            &:nth-child(5) {
                width: 12.5%;
            }
            &:nth-child(6) {
                width: 12.5%;
            }
        }
    }
}
button {
    .tooltip-text {
        visibility: hidden;
        font-size: 12px;
        margin-top: 75px;
        background-color: rgba($color: #000000, $alpha: 0.5);
        color: #fff;
        text-align: center;
        padding: 5px;
        border-radius: 6px;
        position: absolute;
    }
    &:hover {
        .tooltip-text {
            visibility: visible;
    }
}
}
</style>
