<template>
<main :class="$style['content-wrapper']">
    <TheHeader show-heading :toolbar-items="toolbarItems" :class="$style.header" />
    <div :class="$style['reports-view']">
        <TheDeviceSensorHistoryList v-model="selectedDeviceAndGroup" :device-names="filteredDevices" :devices="devices" :loading="getDeviceGroupsDevicesResult.fetching.value"
                                    class="rounded-0" :class="$style['device-sensor-history-list']"
                                    @groupSelected="onGroupSelected" />
        <TheDeviceSensorHistoryContent v-model="selectedDevice" :selected-group="selectedGroup" class="rounded-0" :class="$style['device-sensor-history-content']" />
    </div>
</main>
</template>

<script setup lang="ts">
import { ref, computed } from "vue"
import { watch, watchEffect } from "vue"
import { markRaw } from "vue"

import { useQuery } from "@urql/vue"
import { useRoute, useRouter, type LocationQueryValue } from "vue-router"
import {  toast } from "vuetify-sonner"

import { useUserStore } from "@/stores/UserStore"
import { useDeviceGroupsInfo } from "@/composables/useDeviceGroups"

import { ToolbarItemType, type ToolbarItem, DropdownItemType } from "@/components/TheHeader.vue"
import { MeasurementType, type Device, type GetDeviceGroupsDevicesQuery, type GetDeviceGroupsDevicesQueryVariables, GetDeviceGroupsDevicesDocument } from "@/generated/graphql"
import { type DisplayableDevice } from "@/graphql/types"
import { IconType } from "@/components/SvgIcon.vue"

import TheHeader from "@/components/TheHeader.vue"
import TheDeviceSensorHistoryList, { type SelectedDeviceAndGroup } from "@/components/TheDeviceSensorHistoryList.vue"
import TheDeviceSensorHistoryContent from "@/components/TheDeviceSensorHistoryContent.vue"

import { isDisplayableDevice } from "@/utils"
import type { Maybe } from "graphql/jsutils/Maybe"
import TheNotificationsButton from "@/components/TheNotificationsButton.vue"

const { accountId, tokenParsed, logout } = useUserStore()
const router = useRouter()
const route = useRoute()
const selectedDeviceId = route.params.id
const selectedGroupParam = route.params.group
// const selectedDataMode = ref<string>('')
const toastShown = ref<boolean>(false)

const { selectedDeviceGroupInfo, deviceGroupsInfo, deviceGroupsListItems, fetching: deviceGroupsFetching } = useDeviceGroupsInfo()

// const handleChangeMode = (changeMode: string) => {
//     selectedDataMode.value = changeMode
// };

const selectedDevice = ref<DisplayableDevice>()
const selectedGroup = ref<MeasurementType>()
const selectedDeviceAndGroup = computed<SelectedDeviceAndGroup>({
    get: () => ({
        device: selectedDevice.value,
        group: selectedGroup.value,
    }),
    set: (value: SelectedDeviceAndGroup) => {
        selectedDevice.value = value.device
        selectedGroup.value = value.group
    },
})

const getDeviceGroupsDevicesResult = useQuery<GetDeviceGroupsDevicesQuery, GetDeviceGroupsDevicesQueryVariables>({
    query: GetDeviceGroupsDevicesDocument,
    variables: computed(() => ({
        accountId: accountId!,
        deviceGroupId: selectedDeviceGroupInfo.value?.id ?? "",
    })),
    pause: computed(() => accountId === undefined || selectedDeviceGroupInfo.value === undefined),
})
const filteredDevices = computed(() => {
    if (getDeviceGroupsDevicesResult.data) {
        return getDeviceGroupsDevicesResult.data.value?.account.deviceGroup?.devices;
    } else {
        return [];
    }
});
const deviceGroup = computed<Maybe<GetDeviceGroupsDevicesQuery['account']['deviceGroup']>>(() => {
    return getDeviceGroupsDevicesResult.data.value?.account.deviceGroup
})
const devices = computed<Array<DisplayableDevice>>(() => {
    return (deviceGroup.value?.devices as Maybe<Array<Device>>)?.filter(isDisplayableDevice) as DisplayableDevice[] ?? []
})
const deviceGroupFetchedWithData = computed(() => {
    return getDeviceGroupsDevicesResult.data.value && !getDeviceGroupsDevicesResult.fetching.value
})
const onGroupSelected = (group: MeasurementType) => {
    selectedGroup.value = group
}

watchEffect(() => {
    if (selectedDevice.value == null && selectedDeviceId !== undefined) {
        selectedDevice.value = devices.value.find((device: DisplayableDevice) => device.id === selectedDeviceId)
        if (selectedGroupParam != null)
            selectedGroup.value = MeasurementType[selectedGroupParam as MeasurementType]
    }
})
watch(selectedDevice, (value) => {
    if (value?.id !== selectedDeviceId) {
        const routeQueryParams = Object.entries(route.query).
            map(([ key, value ]: [ string, LocationQueryValue | Array<LocationQueryValue> ]) => `${key}=${value}`)
        router.replace(`/history?${routeQueryParams.join("&")}`)
    }
})

watch(selectedDeviceGroupInfo, () => {
    selectedDeviceAndGroup.value = {
        device: undefined,
        group: undefined,
    }
})

watchEffect(() => {
    if (!toastShown.value && deviceGroupFetchedWithData.value !== undefined && route.params.id && selectedDevice.value === undefined) {
        //@ts-ignore
        toast("Please make sure you have selected the correct group!", {
            cardProps: {
                color: 'warning',
            },           
        });
        toastShown.value = true;
    }
})

const toolbarItems = computed<Array<ToolbarItem>>(() => [
    {
        type: ToolbarItemType.DROPDOWN,
        id: "profile",
        color: "primary",
        icon: {
            type: IconType.MATERIAL,
            category: "filled",
            name: "account_box",
            alt: "Profile menu",
        },
        items: [
            {
                type: DropdownItemType.BUTTON,
                id: "switch_acc",
                name: "Switch Account",
                onClick: () => { router.push("/account-select") },
            },
            {
                type: DropdownItemType.BUTTON,
                id: "logout",
                name: "Logout",
                onClick: () => { logout() },
            },
        ],
        name: tokenParsed?.name,
    },
    {
        type: ToolbarItemType.DROPDOWN,
        id: "groups",
        color: "primary",
        icon: {
            type: IconType.MATERIAL,
            category: "filled",
            name: "people",
            alt: "people",
        },
        loading: deviceGroupsFetching.value,
        name: selectedDeviceGroupInfo.value?.name ?? deviceGroupsInfo.value?.[0]?.name ?? "[no groups]",
        items: deviceGroupsListItems.value,
    },
    {
        type: ToolbarItemType.CUSTOM,
        component: {
            component: markRaw(TheNotificationsButton),
            props: {
                message: "notifications",
            },
        },
        id: "notifications",
    },
])
</script>

<style lang="scss" module>
@import "../assets/styles/variables.scss";
@import "../assets/styles/global.scss";

.content-wrapper {
    @extend .content-wrapper;
    display: flex;
    flex-direction: column;
    height: 100%;
    overflow-y: hidden;
    background-image: url("@/assets/images/background.png");
    background-repeat: repeat;
    .header {
        flex: 0 1 auto;
    }

    .reports-view {
        display: flex;
        flex: 1 1 auto;
        gap: 16px;
        overflow-y: inherit;

        .device-sensor-history-list {
            flex-basis: 30%;
            overflow-y: inherit;
        }

        .device-sensor-history-content {
            flex-basis: 70%;
        }
    }
    @media screen and (max-width: 1000px) {
        .reports-view {
            flex-direction: column;
            align-items: stretch;
            overflow-y: scroll;
            overflow-x: scroll;
            gap:0px;
            @media screen and (max-width: 600px) {
                margin-top: 70px;
            }

            .device-sensor-history-list {
                max-width: 100vw;
                min-height: 80vh;
                flex-basis: auto;
                overflow: scroll;
                order: 1;
                padding: 0;
            }

            .device-sensor-history-content {
                overflow: scroll;
                max-width: 100vw;
                min-height: 100vh;
                flex-basis: auto;
                order: 2;
                padding: 0;
            }
        }
    }
}
</style>
