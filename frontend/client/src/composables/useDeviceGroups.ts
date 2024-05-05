import { computed } from "vue"

import { storeToRefs } from "pinia"
import { useQuery } from "@urql/vue"

import { useGlobalStore } from "@/stores/GlobalStore"
import { useUserStore } from "@/stores/UserStore"

import { DropdownItemType, type DropdownItem } from "@/components/TheHeader.vue"
import { GetDeviceGroupsInfoDocument, type DeviceGroup, type Query } from "@/generated/graphql"

export type DeviceGroupInfo = Pick<DeviceGroup, "id" | "name">

export const useDeviceGroupsInfo = () => {
    const { accountId } = storeToRefs(useUserStore()) 
    const { selectedDeviceGroupId } = storeToRefs(useGlobalStore())
    const { setSelectedDeviceGroupId } = useGlobalStore()
    const selectedDeviceGroupInfo = computed<DeviceGroupInfo | undefined>(() => deviceGroupsInfo.value?.find((deviceGroup => deviceGroup.id === selectedDeviceGroupId.value)))
    const deviceGroupsListItems = computed<Array<DropdownItem>>(() =>
        deviceGroupsInfo.value?.
            filter((deviceGroup: DeviceGroup) => deviceGroup.id != null && deviceGroup.name != null).
            map((deviceGroup: DeviceGroup) => ({
                type: DropdownItemType.BUTTON,
                id: deviceGroup.id,
                name: deviceGroup.name,
                onClick: () => {
                    setSelectedDeviceGroupId(deviceGroup.id)
                },
            } as DropdownItem)) ?? []
    )

    const deviceGroupsResult = useQuery<Query>({
        query: GetDeviceGroupsInfoDocument,
        variables: {
            accountId,
        },
    })
    const deviceGroupsInfo = computed(() => {
        return deviceGroupsResult.data.value?.account.deviceGroups
    })

    return { selectedDeviceGroupInfo, setSelectedDeviceGroupId, deviceGroupsInfo, deviceGroupsListItems, fetching: deviceGroupsResult.fetching }
}
