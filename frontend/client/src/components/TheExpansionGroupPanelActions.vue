<template>
<div class="d-flex justify-space-between gap-5">
    <span class="d-flex flex-column align-center">
        <v-btn disabled size="small" class="v-btn--icon">
            <SvgIcon :type="IconType.MATERIAL" category="round" name="edit" alt="edit icon" :class="pickCssClass('beehive-btn-icon', $style)" />
        </v-btn>
        <span :class="pickCssClass('beehive-btn-text', $style)">Edit Name</span>
    </span>
    <span class="d-flex flex-column align-center">
        <v-btn disabled size="small" class="v-btn--icon">
            <SvgIcon :type="IconType.MATERIAL" category="round" name="delete" alt="delete icon" :class="pickCssClass('beehive-btn-icon', $style)" />
        </v-btn>
        <span :class="pickCssClass('beehive-btn-text', $style)">Delete Group</span>
    </span>
    <span class="d-flex flex-column align-center">
        <v-dialog v-model="showAssignDialog" :class="extractCssClass('group-select-dialog', $style)">
            <template #activator="{ props: activatorProps }">
                <v-btn v-bind="activatorProps" size="small" class="v-btn--icon" :disabled="selectedDevice == null">
                    <SvgIcon :type="IconType.MATERIAL" category="round" name="sync" alt="reassign icon" :class="pickCssClass('beehive-btn-icon', $style)" />
                </v-btn>
                <span :class="pickCssClass('beehive-btn-text', $style)">Reassign</span>
            </template>
            <v-card>
                <template #title>
                    Reassign to
                </template>
                <template #text>
                    <div :class="extractCssClass('group-select-dialog__card-body', $style)">
                        <v-list v-model="selectedGroup">
                            <v-list-item v-for="group in deviceGroups" :key="group.id" :value="group.id" @click="assignDeviceToGroup(group.id)">{{ group.name }}</v-list-item>
                        </v-list>
                    </div>
                </template>
                <div v-show="fetching" :class="extractCssClass('loading-overlay', $style)">
                    <v-progress-circular indeterminate color="" />
                </div>
            </v-card>
        </v-dialog>
    </span>
</div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from "vue"

import { useMutation, useQuery, type OperationResult, type AnyVariables } from "@urql/vue"

import { useUserStore } from "@/stores/UserStore"

import { AssignDeviceToGroupDocument, GetDeviceGroupsInfoDocument, type Query } from "@/generated/graphql"
import type { AssignDeviceToGroupMutation, DeviceGroup, Device } from "@/generated/graphql"

import SvgIcon, { IconType } from "@/components/SvgIcon.vue"

import { extractCssClass, pickCssClass } from "@/utils"

const selectedDevice = defineModel<Device>()

const { accountId } = useUserStore()
const showAssignDialog = ref(false)
const selectedGroup = ref<number | string>()
const fetching = ref(false)

const deviceGroupsResult = useQuery<Query>({
    query: GetDeviceGroupsInfoDocument,
    variables: {
        accountId,
    },
})

const deviceGroups = computed<Array<DeviceGroup>>(() => deviceGroupsResult.data.value?.account.deviceGroups ?? [])
const assignDeviceToGroupMutation = useMutation<AssignDeviceToGroupMutation>(AssignDeviceToGroupDocument)
const assignDeviceToGroup = (groupId: string) => {
    if (selectedDevice.value == null)
        return
    fetching.value = true
    assignDeviceToGroupMutation.executeMutation({
        accountId,
        deviceGroupId: groupId,
        deviceId: selectedDevice.value.id,
    }).then(({ error }: OperationResult<AssignDeviceToGroupMutation, AnyVariables>) => {
        if (error !== null)
            fetching.value = false
        showAssignDialog.value = false
    }).catch(() => {
        fetching.value = false
    })
}
watch(selectedDevice, () => {
    fetching.value = false
    showAssignDialog.value = false
})
</script>

<style lang="scss" module>
@import "../assets/styles/variables";
@import "../assets/styles/functions";

.beehive-btn {
    &-icon {
        transform: scale(calc-svg-scale(24));
    }
    &-text {
        color: rgba(var(--v-theme-on-surface), .5);
        font-size: map-get($font-sizes, '6');
    }
}
.group-select-dialog {
    $max-height: 300px;
    :global {
        .v-overlay__content {
            width: auto;
            min-width: 400px;
        }
    }
    &__card-body {
        position: relative;
        max-height: $max-height;
        :global {
            .v-list {
                max-height: $max-height;
                overflow-y: auto;
            }
        }
    }
    .loading-overlay {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba($color: #000000, $alpha: .1);
        display: grid;
        align-content: center;
        justify-content: center;
    }
}
</style>
