<template>
<v-menu :close-on-content-click="false">
    <template #activator="{ props: activatorProps }">
        <v-btn v-if="result.fetching.value" color="primary-darken-2" v-bind="activatorProps" class="v-btn--icon">
            <v-progress-circular />
        </v-btn>
        <v-btn v-else color="primary-darken-2" v-bind="activatorProps" class="v-btn--icon">
            <SvgIcon v-if="lastFiveNotifications.length !== 0" class="icon-white"  :type="IconType.MATERIAL" category="round" name="notifications" alt="notification-icon" />
            <SvgIcon v-if="lastFiveNotifications.length === 0" class="icon-white" :type="IconType.MATERIAL" category="outlined" name="notifications_none" alt="notifications_round" />
        </v-btn>
    </template>
    <v-list v-if="lastFiveNotifications.length !== 0" :class="extractCssClass('notification-list', $style)">
        <div style="overflow: scroll; height: 500px;">
            <v-list-item v-for="notification in lastFiveNotifications" :key="notification.id">
                <v-card :class="extractCssClass('notification-card', $style)">
                    <div :class="extractCssClass('notification-timestamp', $style)">{{ formatDateTime(notification?.when) }}</div>
                    <template #text>
                        <div style="margin-top: 10px; font-size: 15px;">
                            <SvgIcon :type="IconType.MATERIAL" category="round" name="notifications" alt="notification-icon" class="mr-1" />

                            Device <span style="font-weight: bold;">{{ notification?.deviceId }}</span>
                            {{ getOperatorMessage(notification?.threshold?.operator) }}
                            <span style="font-weight: bold;">{{ notification?.threshold?.threshold }}
                                {{ notification?.threshold?.measurementType && getMeasurementUnit(notification?.threshold?.measurementType) }}</span> {{ getMeasurementType(notification?.threshold?.measurementType) }}
                        </div>
                        <div class="mt-1" style="color: gray;">It's now <span style="font-weight: bold; color: darkred">{{ notification?.threshold?.measurementValue }} {{ notification?.threshold?.measurementType && getMeasurementUnit(notification?.threshold?.measurementType) }}</span></div>
                    </template>                
                    <div v-if="showReportButton" style="font-size: 13px; margin-bottom: 5px;" :class="$style['action-btn']">
                        <v-btn size="small" class="v-btn--icon" :disabled="isDisabledGoToHistory" @click="goToHistory(notification?.deviceId, notification?.threshold?.measurementType)">
                            <SvgIcon :type="IconType.MATERIAL" category="filled" name="history" alt="Show reports" />
                        </v-btn>
                        <span style="margin-left: 5px">Reports</span>
                    </div>
                </v-card>
            </v-list-item>
        </div>
        <RouterLink to="/notifications" style="text-decoration: none; color: inherit;">
            <v-btn :class="extractCssClass('see-more-button', $style)">
                <span style="color: white; text-decoration: none;">See more</span>
            </v-btn>
        </RouterLink>
    </v-list>
    <v-list v-else>
        <v-card :class="extractCssClass('notification-card', $style)">
            <div style="padding: 15px;">No notifications</div>
        </v-card>
        <div :class="extractCssClass('no-notifications', $style)">
            <RouterLink to="/notifications" style="text-decoration: none; color: inherit;">
                <v-btn :class="extractCssClass('see-more-button', $style)">
                    <span style="color: white; text-decoration: none;">See more</span>
                </v-btn>
            </RouterLink>
        </div>
    </v-list>
</v-menu>
</template>

<script setup lang="ts">
import { ref, watchEffect } from "vue"

import SvgIcon, { IconType } from "./SvgIcon.vue"

import { extractCssClass } from "@/utils"
import { useQuery, useSubscription } from "@urql/vue"
import { SubscribeNotificationsDocument, type GetNotificationHistoryQuery, type SubscribeNotificationsSubscription, type SubscribeNotificationsSubscriptionVariables, type GetNotificationHistoryQueryVariables, GetNotificationHistoryDocument, type Maybe } from "@/generated/graphql"
import { computed } from "vue"
import { useSubscriptionToken } from "@/composables/useSubscriptionToken"
import { useUserStore } from "@/stores/UserStore"
import { useDeviceGroupsInfo } from "@/composables/useDeviceGroups"
import { useRouter } from "vue-router"
import type { DisplayableDevice } from "@/graphql/types"

const { accountId } = useUserStore()
const { selectedDeviceGroupInfo } = useDeviceGroupsInfo()
const router = useRouter()
const device = defineModel<DisplayableDevice>()
const currentRoute = router.currentRoute.value.path;
const showReportButton: boolean = !currentRoute.includes('/history');

const lastFiveNotifications = ref<Array<any>>([]);
const subscriptionToken = useSubscriptionToken()
// const notificationsSubscription = useSubscription<SubscribeNotificationsSubscription, SubscribeNotificationsSubscriptionVariables>({
//     query: SubscribeNotificationsDocument,
//     variables: {
//         token: subscriptionToken.value!,
//     },
//     pause: computed(() => subscriptionToken.value === undefined),
// })
const getOperatorMessage = (operator: any) => {
    return operator === 'GREATER_THAN' ? "exceeded" :
        operator === 'LESS_THAN' ? "is below" :
        operator === 'EQUAL' ? "is equal to" : 'Operator Not Specified';
};
const getMeasurementType = (measurementType: any) => {
    return measurementType === 'AIR_PRESSURE' ? "Air Pressure" :
        measurementType === 'TEMPERATURE' ? "Temperature"  :
        measurementType === 'HUMIDITY' ? "Humidity"  :
        measurementType === 'BATTERY_VOLTAGE' ? "Battery Voltage"  :
        measurementType === 'RSRP' ? "Rsrp"  : 'No measurement type'
}
const formatDateTime = (dateTime: any) => {
    return dateTime ?
        new Date(dateTime).toLocaleString('en-GB', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
        }).replace(/\//g, '.') :
        '';
};
const measurementUnits = {
    BATTERY_VOLTAGE: "Millivolts",
    AIR_PRESSURE: "kPa",
    HUMIDITY: "%",
    TEMPERATURE: "°C",
    RSRP: "dBm",
} as const;

type MeasurementType = keyof typeof measurementUnits;

const getMeasurementUnit = (measurementType: MeasurementType): string => {
    return measurementUnits[measurementType] ?? ''; // Return the unit or an empty string if not found
};
const isDisabledGoToHistory = computed(() => {
    return (device.value?.measurement === undefined)
})
function goToHistory(deviceId: any, measurementType: any) {
    router.push(`/history/${deviceId}/${selectedDeviceGroupInfo.value?.id}/${measurementType}`)
}
const result = useQuery<GetNotificationHistoryQuery, GetNotificationHistoryQueryVariables>({
    query: GetNotificationHistoryDocument,
    variables: computed(() => ({
        groupId: selectedDeviceGroupInfo?.value?.id ?? "",
        accountId: accountId ?? "",
    })),
    pause: computed(() => accountId === undefined || selectedDeviceGroupInfo.value?.id === undefined),
})

watchEffect(() => {
    const notifications = result.data?.value?.account?.deviceGroup?.notificationHistory;
    lastFiveNotifications.value = notifications?.slice(0, 5) ?? [];
});
</script>

<style lang="scss" module>
@import "../assets/styles/functions";

.notification-list {
    margin-top:5px;
    display: flex;
    width: 350px;
    align-items: center;
    flex-direction: column;

    :global {
        .v-list-item {
            width: 100%;
            padding: 0 !important;
        }
    }
}

.notification-card {
    position: relative;
    padding: 0rem;
    box-shadow: rgb(100 100 111 / 20%) 0 7px 29px 0;
    margin-bottom: 2px;
    text-align: center;

    &__close-btn {
        position: absolute;
        width: 24px;
        height: 24px;
        top: 50%;
        right: 0;
        transform: translateY(-50%);
        border-radius: 50%;

        .close-icon{
            transform: scale(calc-svg-scale(12));
            opacity: 0.5;
        }
    }

    :global {
        .v-card {
            &-item {
                padding: 0;
            }

            &__title {
                padding: 0;
                font-weight: bold;
                opacity: 0.5;
            }

            &__subtitle {
                padding: 0;
                font-weight: bold;
                font-size: 14px;
                opacity: 0.5;
            }
        }
    }
}
.see-more-button {
    padding: 5px 32px;
    margin: 4px 2px; 
    cursor: pointer; 
    border-radius: 20px; 
    background-color: #707cd4;
}
.see-more-button:hover {
    background-color: #5060c7;

    transform: scale(1.02);
    transition: transform 0.4s ease; 
    }

.notification-timestamp {
    font-size: 13px;
    position: absolute;
    top: 5px;
    right: 10px;
    color: rgba(var(--v-theme-on-surface), .5);
}


:global {
    .v-list {
        padding: 0;
    }
}

.no-notifications {
    text-align: center;
}
</style>
