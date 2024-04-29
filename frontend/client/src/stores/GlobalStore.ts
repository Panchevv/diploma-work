import { defineStore } from "pinia"
import type { DeviceGroup } from "@/generated/graphql"

export type GlobalStoreState = {
  selectedDeviceGroupId: DeviceGroup["id"] | undefined,
  locale: string,
}
export const useGlobalStore = defineStore("globalStore", {
    state: () => ({
        selectedDeviceGroupId: undefined,
    } as GlobalStoreState),
    getters: {
    },
    actions: {
        setSelectedDeviceGroupId(deviceGroupId: DeviceGroup["id"]) {
            this.selectedDeviceGroupId = deviceGroupId
        },
        setLocale(locale: string) {
            this.locale = locale
        },
    },
    persist: {
        storage: localStorage,
        paths: [ 'selectedDeviceGroupId', 'locale' ],
    },
})
