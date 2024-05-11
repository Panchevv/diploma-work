<template>
<main :class="pickCssClass(`content-wrapper`, $style)">
    <div class="d-flex flex-column">
        <TheHeader show-heading :toolbar-items="toolbarItems" :class="pickCssClass('notifications-toolbar', $style)" style="margin-bottom: -10px;" />
        <div v-if="showHadingFromBeehives" :class="extractCssClass('back-button-container', $style)">
            <v-btn :class="extractCssClass('back-button', $style)" @click="backToBeehives">
                <SvgIcon class="icon-white" :type="IconType.MATERIAL" category="filled" name="back" alt="plus" />
            </v-btn>
        </div>
        <h1 :class="extractCssClass('heading', $style)">
            Notifications
            <span v-if="showHadingFromBeehives"> - {{ groupName }}</span>
        </h1>
        <v-progress-linear v-if="!notificationHistory" class="flex-0-1 mt-1" />

        <div v-if="hasNotifications" class="d-flex align-center flex-column" :class="extractCssClass('card-container', $style)">
            <div :class="extractCssClass('notification-card-container', $style)">
                <div v-for="notification in notificationHistory" :key="notification?.when">
                    <v-card :class="pickCssClass('notification-card', $style)" class="my-7">
                        <div :class="extractCssClass('notification-timestamp', $style)">{{ formatDateTime(notification?.when) }}</div>
                        <template #title>
                            <div v-if="!isSmallScreen" style="border-bottom:1px solid #ccc; width: 720px; margin-bottom: 5px;">
                                <SvgIcon :type="IconType.MATERIAL" category="round" name="notifications" alt="notification-icon" class="mr-1" />
                                Device 
                                <span style="font-weight: bold;">{{ notification?.deviceId }}</span>
                                {{ getOperatorMessage(notification?.threshold?.operator) }}
                                <span style="font-weight: bold;">{{ notification?.threshold?.threshold }}
                                    {{ notification?.threshold?.measurementType && getMeasurementUnit(notification?.threshold?.measurementType) }}</span> {{ getMeasurementType(notification?.threshold?.measurementType) }}
                            </div>
                        </template>
                        <template #text>
                            Device <span style="font-weight: bold;">{{ notification?.deviceId }}
                            </span> {{ getOperatorMessage(notification?.threshold?.operator) }} "{{ notification?.threshold?.thresholdName }}" Threshold of
                            {{ notification?.threshold?.threshold }}
                            {{ notification?.threshold?.measurementType && getMeasurementUnit(notification?.threshold?.measurementType) }} {{ getMeasurementType(notification?.threshold?.measurementType) }} and it is now
                            <span style="font-weight: bold; color: darkred"> {{ notification?.threshold?.measurementValue }} {{ notification?.threshold?.measurementType && getMeasurementUnit(notification?.threshold?.measurementType) }}</span>
                        </template>
                        <div :class="extractCssClass('go-to-history-button', $style)">
                            <v-btn size="small" class="v-btn--icon mb-2" @click="goToHistory(notification?.deviceId, notification?.threshold?.measurementType)">
                                <SvgIcon :type="IconType.MATERIAL" category="filled" name="history" alt="Show reports" />
                            </v-btn>
                            <span v-if="!isSmallScreen" style="margin-left: 5px">Reports</span>
                        </div>
                    </v-card>
                </div>
            </div>
        </div>
        <div v-else style="display: flex; justify-content: center; font-size: 25px; height: 50vh; align-items: center;">No Notificaions.</div>
    </div>
</main>
</template>

<script setup lang="ts">
import { computed, watchEffect, ref } from 'vue';

import { useQuery } from '@urql/vue';

import { useDeviceGroupsInfo } from '@/composables/useDeviceGroups';
import { useUserStore } from '@/stores/UserStore';

import SvgIcon, { IconType } from "@/components/SvgIcon.vue"
import TheHeader, { ToolbarItemType, type ToolbarItem, DropdownItemType } from '@/components/TheHeader.vue';
import {  GetNotificationHistoryDocument, type GetNotificationHistoryQuery, type GetNotificationHistoryQueryVariables, MeasurementType, type Maybe } from '@/generated/graphql';

import { extractCssClass, pickCssClass } from '@/utils';
import { useRoute, useRouter } from 'vue-router';

const { keycloak, logout } = useUserStore()
const { accountId } = useUserStore()
const router = useRouter()

const { selectedDeviceGroupInfo, deviceGroupsInfo, deviceGroupsListItems, fetching: deviceGroupsFetching } = useDeviceGroupsInfo()
const route = useRoute();
let fromBeehive = route.params.fromBeehive
const groupId =  route.params.id;
const groupName = route.params.name;
let groupIdNotif = ref<any>("")
const showHadingFromBeehives = ref<boolean>(false)
const isSmallScreen = window.innerWidth < 900; 

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

const backToBeehives = () => {
    router.back();
}
function goToHistory(deviceId: any, measurementType: any) {   
    router.push(`/history/${deviceId}/${selectedDeviceGroupInfo.value?.id}/${measurementType}`)
}
watchEffect(() => {
    groupIdNotif.value = fromBeehive === "true" ? groupId : selectedDeviceGroupInfo.value?.id;

    if (fromBeehive === "true"){
        showHadingFromBeehives.value = true
    }    
    
});

const result = useQuery<GetNotificationHistoryQuery, GetNotificationHistoryQueryVariables>({
    query: GetNotificationHistoryDocument,
    variables: computed(() => ({
        groupId: groupIdNotif.value ?? "",
        accountId: accountId ?? "",
    })),
    pause: computed(() => accountId === undefined || groupIdNotif.value  === undefined),
})
const notificationHistory = computed(() => result.data.value?.account.deviceGroup?.notificationHistory)
const hasNotifications = computed(() => notificationHistory.value?.length !== 0)

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

const getMeasurementUnit = (measurementType: MeasurementType): string => {
    return measurementUnits[measurementType] ?? ''; 
};

const toolbarItems = computed<Array<ToolbarItem>>(() => {
    const baseItems = [
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
            name: keycloak.tokenParsed?.name,
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
    ];
    if (fromBeehive === 'true') {
        return baseItems.filter(item => item.id !== 'groups');
    }

    return baseItems;
});
</script>

<style lang="scss" module>
.back-button-container {
    margin-bottom: 15px;

    @media only screen and (max-width: 600px) {
        margin-top: 80px;
        margin-bottom: -80px;
    }
}
.notifications-toolbar {
    @media only screen and (max-width: 600px) {
        margin-right: 24px;
    }
}

.content-wrapper {
    margin-right: 25px;
    margin-left: 25px;
    background-image: url("@/assets/images/background.png");
    background-repeat: repeat;
    @media only screen and (max-width: 600px) {
        margin-left: 20px;
        width: 90vw;
        height: 80vh;
    }
}

.heading {
    color: rgba(var(--v-theme-on-surface), .5);
    @media only screen and (max-width: 600px) {
        margin-top: 90px;
        margin-bottom: 10px;
    }
}
.card-container {
    overflow-y: scroll;
    height: 77vh;
    box-shadow: 0 2px 4px -1px rgba(0,0,0,.2), 0 4px 5px 0 rgba(0,0,0,.14), 0 1px 10px 0 rgba(0,0,0,.12);
    border-radius: 0.5rem;
    min-width: 650px;
    background-color: rgba(245, 245, 245, 0.6);


    @media only screen and (max-width: 750px) and (min-width: 600px) {
        min-width: 70vw;
        max-width: 80vw;
        font-size: 15px;
        padding: 0px;
        margin-right:0px;
    }
    @media only screen and (max-width: 600px) {
        min-width: 89vw;
        max-width: 70vw;
        font-size: 15px;
        padding: 0px;
        padding-bottom: 5px;
        margin-right: -20px;
        margin-left: 0px;
        height: 70vh;
        min-height: 65vh;

    }
}
.go-to-history-button{
    font-size: 13px;
    position: absolute;
    top: 35px;
    right: 20vw;
    @media only screen and (max-width: 1530px) {
        top: 50px;
        right: 5vw;
    }    

    @media only screen and (max-width: 1090px) and (min-width: 1000px)   {
        top: 50px;

        right: 1vw;
    }
    @media only screen and (max-width: 1000px) {
        top: 100px;
        right: 25vw;
    }
} 

.notification-card {
    border-radius: 30px; 
    box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.5), 0 4px 5px 0 rgba(0,0,0,.14), 0 1px 10px 0 rgba(0,0,0,.12);
    justify-content: start;
    width: 85vw;
    padding: 10px;
    min-width: 400px;
    
    @media only screen and (max-width: 1000px) {
        width: inherit;
        min-width: 40vw;
        margin-bottom: 5px;
        height: 140px;
    }
}
.notification-timestamp {
    position: absolute;
    top: 5px;
    right: 25px;
    color: rgba(var(--v-theme-on-surface), .5);
}

.back-button {
    padding: 5px 32px;
    margin: 4px 2px; 
    cursor: pointer; 
    border-radius: 8px; 
    background-color: #707cd4;
}
.back-button:hover {
    background-color: #5060c7;

    transform: scale(1.1);
    transition: transform 0.4s ease; 

    }
</style>