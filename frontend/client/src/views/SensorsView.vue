<template>
<main :class="extractCssClass('content-wrapper', $style)">
    <TheHeader show-heading :toolbar-items="toolbarItems" :class="$style.header" />
    <section :class="extractCssClass('sensors-view', $style)">
        <h1 :class="extractCssClass('manage-groups-heading', $style)">Manage Groups</h1>
        <v-progress-linear v-if="fetching" class="flex-0-1 mt-1" />
        <div v-else :class="extractCssClass('sensors-view__content', $style)">
            <div class="d-flex flex-0-1 justify-space-between">
                <div style="width: 300px;">
                    <v-text-field v-model="filter" variant="underlined" hide-details placeholder="Search a Group">
                        <SvgIcon :type="IconType.MATERIAL" category="round" name="search" alt="magnifying_glass" />
                    </v-text-field>
                </div>
                <div>
                    <v-dialog v-model="dialog" persistent width="420">
                        <template #activator="{ props: activatorProps }">
                            <v-btn v-bind="activatorProps" color="surface" class="v-btn--icon">
                                <SvgIcon :type="IconType.MATERIAL" category="filled" name="add_group" alt="add_group" />
                            </v-btn>
                            <span :class="pickCssClass('add-btn-text', $style)">Add Group</span>
                        </template>
                        <v-card style="padding: 1.5rem; align-items: center;">
                            <v-card-title>
                                <p style="text-align: center;" class="text-h5">Add a Group</p>
                                <hr />
                            </v-card-title>
                            <v-card-text>
                                <p style="color:gray">Please enter the name of the group:</p>
                            </v-card-text>
                            <div style="width: 250px" class="mt-2 mb-2">
                                <v-text-field v-model="groupName" hide-details placeholder="Group Name*" />
                            </div>
                            <v-card-actions style="width: 60%;display: flex; justify-content: space-evenly" class="mt-2">
                                <v-btn
                                    style="background-color: #1976D2"
                                    color="white"
                                    variant="text"
                                    class="mr-4 w-50"
                                    :disabled="!groupName"
                                    @click="createGroup">
                                    Save
                                </v-btn>
                                <v-btn
                                    color="blue-darken-1"
                                    style="border: 1px solid #1976D2 "
                                    variant="text"
                                    @click="dialog = false; groupName = ''">
                                    Cancel
                                </v-btn>
                            </v-card-actions>
                        </v-card>
                    </v-dialog>
                </div>
            </div>
            <v-expansion-panels :class="pickCssClass('groups-container', $style)">
                <v-expansion-panel v-for="deviceGroup in filteredGroupData" :key="deviceGroup.id" :title="deviceGroup.id" class="mb-4">
                    <template #title>
                        <div class="d-flex align-center">
                            <div :class="pickCssClass('title', $style)">
                                {{ deviceGroup.name }}
                            </div>
                            <div :class="pickCssClass('material-opacity counter-container', $style)">
                                <span style="font-weight: bold;">Devices:  {{ deviceGroup.devices?.length }}</span>
                            </div>
                            <div class="d-flex align-center">
                                <template v-for="(actionButton, index) in actionButtons" :key="actionButton.id">
                                    <v-btn
                                        v-if="deviceGroup.name !== 'Ungrouped'"
                                        v-ripple
                                        variant="plain"
                                        :disabled="!actionButton.enabled"
                                        @click.stop="handleActionButtonClick(actionButton, deviceGroup.id, deviceGroup.name ?? '')">
                                        <SvgIcon
                                            v-bind="actionButton.icon" />
                                        <span
                                            :class="pickCssClass('tooltip-text', $style)">
                                            {{ _.capitalize(actionButton.name) }}
                                        </span>
                                    </v-btn>
                                    <div v-if="deviceGroup.name !== 'Ungrouped'">
                                        <span
                                            v-if="((index + 1) % 3 === 0 && index !== actionButtons.length - 1)" style="font-size: 35px; margin-left: -5px; margin-right: 10px;">
                                            |
                                        </span>
                                    </div>
                                </template>
                            </div>
                        </div>
                    </template>
                    <template #text>
                        <TheExpansionGroupPanel :devices="deviceGroup.devices ?? []" @click.stop />
                    </template>
                </v-expansion-panel>
            </v-expansion-panels>
            <div>
                <v-dialog v-model="addSensorDialog" persistent width="420">
                    <v-card style="padding: 1.5rem; align-items: center;">
                        <v-card-title>
                            <p style="text-align: center; " class="text-h6">Add Device to<span style="color: black;  font-weight: bolder;">{{ selectedDeviceGroupName }}</span></p>
                            <hr />
                        </v-card-title>
                        <v-progress-linear v-if="fetchingAddDevice" class="flex-0-1 mt-1" />
                        <div style="width: 300px; margin-top: 30px">
                            <v-text-field v-model="newDeviceName" hide-details placeholder="Device name" />
                        </div>
                        <div style="width: 300px; margin-top: 20px">
                            <v-text-field v-model="newDeviceId" hide-details placeholder="Device ID" />
                        </div>
                        <v-card-actions style="width: 60%;display: flex; justify-content: space-evenly">
                            <v-btn
                                style="background-color: #1976D2"
                                color="white"
                                variant="text"
                                class="w-50"
                                :disabled="addDeviceButtonDisabled"
                                @click="createDevice">
                                Save
                            </v-btn>
                            <v-btn
                                color="blue-darken-1"
                                style="border: 1px solid #1976D2 "
                                variant="text"
                                :disabled="fetchingAddDevice"
                                @click="closeForm">
                                Cancel
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </div>
            <div>
                <v-dialog v-model="renameGroupDialog" persistent width="420">
                    <v-card style="padding: 1.5rem; align-items: center;">
                        <v-card-title>
                            <p style="text-align: center; " class="text-h6">Rename group <span style="color: black;  font-weight: bolder;">{{ selectedDeviceGroupName }}</span></p>
                            <hr class=" mb-5 w-200" />
                        </v-card-title>
                        <div style="width: 300px; margin-top: 5px">
                            <v-text-field 
                                v-model="editedName" 
                                variant="underlined" 
                                prepend-inner-icon="mdi-pencil"
                                class="w-100 mb-5"
                                label="New name" />
                        </div>
                        <v-card-actions style="width: 60%;display: flex; justify-content: space-evenly">
                            <v-btn
                                type="button"
                                style="background-color: #1976D2"
                                color="white"
                                variant="text"
                                class="w-50 "
                                :loading="loadingRenameGroup" 
                                :disabled="renameGroupButtonDisabled"
                                @click="renameGroup">
                                Save
                            </v-btn>
                            <v-btn
                                type="button"
                                color="blue-darken-1"
                                style="border: 1px solid #1976D2 "
                                variant="text"
                                :disabled="loadingRenameGroup"
                                @click="renameGroupDialog = false; editedName = ''">
                                Cancel
                            </v-btn>
                        </v-card-actions>
                    </v-card>
                </v-dialog>
            </div>
        </div>
    </section>
</main>
</template>

<script setup lang="ts">
import {  ref, watchEffect } from "vue"
import { computed } from "vue"

import { useMutation, useQuery, type OperationResult } from "@urql/vue"

import { useUserStore } from "@/stores/UserStore"

import SvgIcon, { IconType, type SvgIconProps } from "@/components/SvgIcon.vue"
import { AddDeviceDocument, CreateDeviceGroupDocument, GetDeviceGroupsDocument, GetUngroupedDevicesDocument, RenameDeviceGroupDocument } from "@/generated/graphql"
import type { DeviceGroup, Device, DeviceEdge, Query, CreateDeviceGroupMutation, CreateDeviceGroupMutationVariables, AddDeviceMutation, AddDeviceMutationVariables, RenameDeviceGroupMutation, RenameDeviceGroupMutationVariables } from "@/generated/graphql"

import TheHeader, { ToolbarItemType, type ToolbarItem, DropdownItemType } from "@/components/TheHeader.vue"
import TheExpansionGroupPanel from "@/components/TheExpansionGroupPanel.vue"

import _ from "lodash"
import { extractCssClass, pickCssClass } from "@/utils"
import { useGlobalStore } from "@/stores/GlobalStore"
import { useRouter } from "vue-router"
import { toast } from "vuetify-sonner"
import { storeToRefs } from "pinia"

const { logout, tokenParsed } = useUserStore()
const { selectedDeviceGroupId } = storeToRefs(useGlobalStore())

const { accountId } = useUserStore()
const router = useRouter()
const newDeviceName = ref<string>("")
const newDeviceId = ref<string>("")
const addDeviceButtonDisabled = computed(() => {
    return !newDeviceName.value || !newDeviceId.value;
});
const dialog = ref(false)
const addSensorDialog = ref(false)
const filter = ref<string>("")
const groupName = ref<string>("")
const selectedGroupId = ref<string>("")
const selectedDeviceGroupName = ref<string>("")
const fromSensors = ref<string>("")
const renameGroupDialog = ref<boolean>(false)
const editedName = ref<string>("")
const loadingRenameGroup = ref<boolean>(false)

const handleActionButtonClick = (actionButton: any, deviceGroupId: string, deviceGroupName: string) => {
    if (actionButton.id === "addSensor") {
        addSensorDialog.value = true;
        selectedDeviceGroupName.value = deviceGroupName;
        selectedGroupId.value = deviceGroupId; 
        
    }
    if (actionButton.id === "notifications") {
        selectedGroupId.value = deviceGroupId; 
        selectedDeviceGroupName.value = deviceGroupName;
        fromSensors.value = "true";
        router.push(`/notifications/${selectedGroupId.value}/${selectedDeviceGroupName.value}/${fromSensors.value}`)
    }
    if (actionButton.id === "editName") {
        selectedGroupId.value = deviceGroupId;
        selectedDeviceGroupName.value = deviceGroupName;
        editedName.value = selectedDeviceGroupName.value
        renameGroupDialog.value = true;
    }
}

const renameGroupButtonDisabled = computed(() => {
    return !editedName.value || (editedName.value === selectedDeviceGroupName.value)
})

function closeForm() {
    addSensorDialog.value = false;
    newDeviceName.value = '';
    newDeviceId.value = ''; 
}

const deviceGroupsResult = useQuery<Query>({
    query: GetDeviceGroupsDocument,
    variables: {
        accountId,
    },
})
const ungroupedDevicesResult = useQuery<Query>({
    query: GetUngroupedDevicesDocument,
    variables: {
        accountId,
    },
})

const renameDeviceGroupMutation = useMutation<RenameDeviceGroupMutation, RenameDeviceGroupMutationVariables>(RenameDeviceGroupDocument)
const renameGroup = () => {
    loadingRenameGroup.value = true
    renameDeviceGroupMutation.executeMutation({
        accountId: accountId! as string,
        groupId: selectedGroupId.value,
        name: editedName.value,
    }).then(({ data, error }: OperationResult<RenameDeviceGroupMutation>) => {
        if (error) {                        
            loadingRenameGroup.value = false
            return
        }
        //@ts-ignore
        toast(`Successfully RENAMED the Group to "${data?.updateDeviceGroup.name}"`, {
            cardProps: {
                color: "success",
            },
        })        
        loadingRenameGroup.value = false
        renameGroupDialog.value = false
    }).catch(() => {
        loadingRenameGroup.value = false
    })
}
const createDeviceGroupMutation = useMutation<CreateDeviceGroupMutation, CreateDeviceGroupMutationVariables>(CreateDeviceGroupDocument)
const createDeviceGroup = () => {
    createDeviceGroupMutation.executeMutation({
        accountId: accountId! as string,
        name: groupName.value,
    }).then(({ error, data }: OperationResult<CreateDeviceGroupMutation>) => {
        selectedDeviceGroupId.value = data?.createDeviceGroup.id;        
        if (error == null) {            
            //@ts-ignore
            toast(`Successfully CREATED Group "${data?.createDeviceGroup.name}"`, {
                cardProps: {
                    color: "success",
                },
            })
        }
    })
}
const createDeviceMutation = useMutation<AddDeviceMutation, AddDeviceMutationVariables>(AddDeviceDocument)
const createDevice = async () => {
    fetchingAddDevice.value = true
    const result = await createDeviceMutation.executeMutation({
        accountId: accountId! as string,
        deviceId: newDeviceId.value,
        groupId: selectedGroupId.value,
        name: newDeviceName.value,
    }).then(({ data, error }: OperationResult<AddDeviceMutation>) => {
        console.error(error);
        
        fetchingAddDevice.value = false
        if (error == null) {            
            closeForm()
            //@ts-ignore
            toast(`Successfully CREATED Device "${data?.createDevice.name}"`, {
                cardProps: {
                    color: "success",
                },
            })
        }
        deviceGroupsResult.executeQuery({ requestPolicy: 'network-only' })
    })
}

const fetching = computed(() => deviceGroupsResult.fetching.value || ungroupedDevicesResult.fetching.value)
const fetchingAddDevice = ref<boolean>(false)
const deviceGroups = computed(() => deviceGroupsResult.data.value?.account.deviceGroups ?? [])
const ungroupedDevices = computed<Array<Device>>(() => ungroupedDevicesResult.data.value?.account.devices?.edges?.map((deviceEdge: DeviceEdge) => deviceEdge.node) ?? [])

const filteredGroupData = computed<Array<DeviceGroup>>(() => {
    const filteredText = filter.value.toLowerCase()
    const ungroupedDevicesGroup: DeviceGroup = {
        id: "ungrouped",
        name: "Ungrouped",
        devices: ungroupedDevices.value,
        device: null,
        notificationHistory: null,
    }
    return [ ...deviceGroups.value, ungroupedDevicesGroup ].filter((deviceGroup: DeviceGroup) => deviceGroup?.name?.toLowerCase().includes(filteredText)) ?? []
})



const actionButtons: Array<{
    id: string
    name: string
    icon: SvgIconProps
    enabled: boolean
}> = [
    {
        id: "editName",
        name: "Edit Name",
        icon: {
            type: IconType.MATERIAL,
            name: "edit",
            category: "round",
            alt: "edit name icon",
        },
        enabled: true,
    },
    {
        id: "deleteGroup",
        name: "Delete Group",
        icon: {
            type: IconType.MATERIAL,
            name: "delete",
            category: "round",
            alt: "delete group icon",
        },
        enabled: false,
    },
    {
        id: "notifications",
        name: "Notifications",
        icon: {
            type: IconType.MATERIAL,
            name: "notifications",
            category: "round",
            alt: "notification icon",
        },
        enabled: true,
    },
    {
        id: "addSensor",
        name: "Add Sensor",
        icon: {
            type: IconType.MATERIAL,
            name: "add_circle",
            category: "filled",
            alt: "addSensor icon",
        },
        enabled: true,
    },

]
const createGroup = () => {
    createDeviceGroup()
    watchEffect(() => {
        if (createDeviceGroupMutation.data.value && !createDeviceGroupMutation.fetching.value) {
            deviceGroupsResult.executeQuery({
                requestPolicy: 'network-only',  
            })
        }
    })
    dialog.value = false
    groupName.value = ''
}

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
])
</script>

<style lang="scss" module>
@import "../assets/styles/variables.scss";
@import "../assets/styles/global.scss";

.content-wrapper {
@extend .content-wrapper;
background-image: url("@/assets/images/background.png");
background-repeat: repeat;
display: flex;
flex-direction: column;
height: 100%;
overflow-y: hidden;

.header {
    flex: 0 1 auto;
}

.sensors-view {
    flex: 1 1 auto;
    overflow-y: inherit;
    display: flex;
    flex-direction: column;
    @media only screen and (max-width: 600px) {
        margin-top: 90px;
    }
    .manage-groups-heading {
        flex: 0 1 auto;
        margin-bottom: 1rem;
        font-family: Roboto, "Helvetica Neue", sans-serif;
        font-weight: 400;
        font-size: 24px;
        color: rgb(var(--v-theme-on-background));
        @media only screen and (max-width: 600px) { 
            font-size: 24px;
        };
    }
    &__content {
        flex: 0 1 auto;
        display: flex;
        flex-direction: column;
        overflow-y: inherit;
        .groups-container {
            flex: 0 1 auto;
            margin-top: 1rem;
            overflow-y: auto;
            @media only screen and (max-width: 950px) {
                width: 232vw;
            }
        }
        :global {
            .v-expansion-panel {
                &-text__wrapper {
                    padding: 0;
                }
                &-title {
                    padding: 0;
                }
                .mdi-chevron-down {
                    padding-right: 5rem;
                }
                .mdi-chevron-up {
                    padding-right: 5rem;
                }
                .v-btn {
                .tooltip-text {
                    visibility: hidden;
                    font-size: 12px;
                    margin-bottom: 40px;
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
            }
        }
    }
}
}
.action-button-divider {
width: 10px;
height: 20px;
margin: 0 10px;
background-color: black;
border: solid;
}   
.title {
display: flex;
justify-content: center;
margin-right: 1rem;
min-width: 225px;
min-height: 5rem;
background-color: rgb(var(--v-theme-secondary));
border-radius: 0.25rem 0 0 0.25rem;
align-items: center;
color: rgb((var(--v-theme-on-secondary)));
font-weight: bold;
@media only screen and (max-width: 600px) { 
    min-height: 4rem;
    font-size: 16px;
};
}
.btn-text {
font-family: Roboto, "Helvetica Neue", sans-serif;
font-weight: bold;
@media only screen and (max-width: 600px) { 
    font-size: 14px;
}
}
.counter-container {
width: 150px;
}

.add-btn-text {
    opacity: 0.3;
    margin-left: 2px;
    pointer-events: none;
    cursor: not-allowed;
    margin-left:8px;
    @media only screen and (max-width: 600px) { 
        display: none;
    }
}
</style>
