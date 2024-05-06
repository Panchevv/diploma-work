<template>
<main :class="extractCssClass('content-wrapper', $style)">
    <div class="d-flex flex-column">
        <TheHeader :class="pickCssClass(`client-toolbar-items`, $style)" show-heading :toolbar-items="toolbarItems" />
        <div class="d-flex justify-space-around">
            <div  :class="pickCssClass(`v-card`, $style)" class="pa-5 pl-10 pr-10" style="background-color: rgba(255, 255, 255, 0.9); width: auto; min-width: 70vw; height: auto; max-height: 83vh; border-radius: 10px; overflow-x: hidden">
                <div class="d-flex justify-center">
                    <span :class="extractCssClass(`user-account-title`, $style)" class="fs-2">SensoScope Account Information</span>
                </div>
                <div class="d-flex flex-row align-center pa-1 mb-7 pr-6" style="background-color: rgba(182, 194, 282, 0.2); height: 27vh; border-radius: 20px;">
                    <div class="d-flex flex-column justify-center align-center" style="flex-grow: 0.5">
                        <img :class="pickCssClass(`user-account-img`, $style)" src="@/assets/images/unknown.jpeg" alt="" />
                    </div>
                    <div class="d-flex flex-column"  :class="pickCssClass(`sensoscope-info-container`, $style)">
                        <div class="d-flex justify-space-between align-center pa-1">
                            <span>SensoScope ID: </span>
                            <span>{{ accountId }}</span>
                        </div>
                        <v-divider class="mb-4" />
                        <div class="d-flex justify-space-between align-center pa-1">
                            <span>SensoScope Name: </span>
                            <span>{{ accountName }}</span>
                        </div>
                        <v-divider class="mb-4" />
                    </div>
                </div>

                <v-divider class="mb-4" /> 

                <div class="d-flex flex-column mt-7">
                    <div class="d-flex justify-between align-center pa-1">
                        <v-btn class="v-btn--icon mt-5" size="medium" @click="showNRFConnection = !showNRFConnection; onNRFEdit = false">
                            <SvgIcon v-if="!showNRFConnection" :type="IconType.MATERIAL" category="round" name="expand_more" alt="expand" />
                            <SvgIcon v-else :type="IconType.MATERIAL" category="round" name="expand_less" alt="hide" />
                        </v-btn>

                        <div class="d-flex justify-center align-center flex-grow-1">
                            <span style=" font-size: 1.4em;" class="mt-3 font-weight-bold"><span v-if="onNRFEdit">Configure </span>NRF Cloud Account<span v-if="showNRFConnection">: </span></span>
                        </div>  
                        <div v-if="showNRFConnection" class="d-flex justify-end align-end">
                            <v-btn v-if="!onNRFEdit" class="v-btn--icon mt-5 "  :class="extractCssClass('tooltip-container', $style)" @click="onNRFEdit = !onNRFEdit">
                                <SvgIcon :type="IconType.MATERIAL" category="outlined" name="edit" alt="edit_icon" />
                                <span :class="extractCssClass('tooltip-text', $style)">Edit Configuration</span>
                            </v-btn>
                        </div>
                    </div>

                    <div v-if="showNRFConnection" class="mb-8 d-flex justify-center align-center">
                        <div>
                            <div style="border-top: 1px solid black; width: 40vw"></div>
                        </div>
                    </div>
                </div>
                <div v-if="showNRFConnection">
                    <div class="d-flex justify-space-between align-center pa-1">
                        <span v-if="!onNRFEdit" :class="extractCssClass(`sub-title`, $style)" style="margin-top: 10px; width: 100px; padding-right: 1rem; white-space: nowrap;">
                            Api Key: 
                        </span>
                        <span v-if="!onNRFEdit" ref="NRFKeyElement" 
                              :style="{ filter: blurNRFKey ? 'blur(5px)' : 'none', pointerEvents: blurNRFKey ? 'none' : 'auto', userSelect: blurNRFKey ? 'none' : 'text' }" 
                              style="margin-top: 10px; white-space: nowrap; text-overflow: ellipsis; overflow: hidden;" :class="extractCssClass(`small-text`, $style)">{{ nrfConfiguration?.nrfCloudSettings?.bearerToken }} </span>
                        <v-text-field v-if="onNRFEdit"
                                      v-model="NRFKeyValue"
                                      class="mb-5"
                                      label="Api Key"
                                      variant="underlined"
                                      hide-details
                                      required 
                                      style="width: 70%"
                                      :style="{ width: onNRFEdit ? '150px' : 'auto', fontSize: onNRFEdit ? '0.8em' : '1em' }" />
                
                        <div class="d-flex justify-end pa-1">
                            <v-avatar v-if="!onNRFEdit" size="25" class="mr-4 mt-2">
                                <v-btn v-if="hasNRFKey" class="mb-1" @click="blurNRFKeyMethod">
                                    <SvgIcon :type="IconType.MATERIAL" category="outlined" :name="blurNRFKey ? 'visibility' : 'visibility_off'" alt="content_hide_icon" />
                                </v-btn>
                            </v-avatar>
                            <v-btn v-if="!onNRFEdit" class="mb-1" :disabled="blurNRFKey" :class="extractCssClass('tooltip-container', $style)" @click="copyNRFKey">
                                <SvgIcon :type="IconType.MATERIAL" category="round" name="content_copy" alt="content_copy_icon" />
                                <span :class="extractCssClass('tooltip-text', $style)">Copy to Clipboard</span>
                            </v-btn>
                        </div>
                    </div>
                    <v-divider v-if="!onNRFEdit" class="mb-4" />

                    <div v-if="onNRFEdit" class="d-flex justify-space-around">
                        <v-btn class="mt-5" variant="text" @click="onNRFEdit = false">Cancel</v-btn>
                        <v-btn class="mt-5" variant="text" :loading="loadingSetNrfAccount" @click="addUpdateNRFConfiguration">Save</v-btn>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
</template>

<script setup lang="ts">
import { watchEffect } from 'vue';
import { computed } from 'vue';
import { ref } from 'vue';

import { useMutation, useQuery } from '@urql/vue';
import { useRouter } from 'vue-router';

import { type SetNrfCloudAccountMutation, type SetNrfCloudAccountMutationVariables, type GetNrfCloudConfigurationQuery, type GetNrfCloudConfigurationQueryVariables, GetNrfCloudConfigurationDocument, SetNrfCloudAccountDocument } from '@/generated/graphql';
import SvgIcon, { IconType } from '@/components/SvgIcon.vue';
import { extractCssClass, pickCssClass } from '@/utils';

import TheHeader, { ToolbarItemType, type ToolbarItem, DropdownItemType } from '@/components/TheHeader.vue';

import { useUserStore } from '@/stores/UserStore';

import { toast } from 'vuetify-sonner';

const { accountId, accountName } = useUserStore()
const { keycloak, logout } = useUserStore()
const router = useRouter()



const blurNRFKey = ref<boolean>(true)
const loadingSetNrfAccount = ref<boolean>(false)
const onNRFEdit = ref<boolean>(false)

function blurNRFKeyMethod() {    
    blurNRFKey.value = !blurNRFKey.value
}

const NRFKeyElement = ref<HTMLElement>(null as unknown as HTMLElement)

const getNRFCloudConfiguration = useQuery<GetNrfCloudConfigurationQuery, GetNrfCloudConfigurationQueryVariables>({
    query: GetNrfCloudConfigurationDocument,
    variables: {
        id: accountId as string,
    },
})

const nrfConfiguration = computed(() => getNRFCloudConfiguration.data.value?.account)

const NRFKeyValue = ref(nrfConfiguration.value?.nrfCloudSettings?.bearerToken ?? '')

const showNRFConnection = ref(false)

watchEffect(() => {
    if (onNRFEdit.value) {
        NRFKeyValue.value = nrfConfiguration.value?.nrfCloudSettings?.bearerToken ?? '';
    }
});

const hasNRFKey = ref(false)

watchEffect(() => {    
    if (nrfConfiguration.value?.nrfCloudSettings?.bearerToken) {
        hasNRFKey.value = true
    }
});

function copyNRFKey() {
    navigator.clipboard.writeText(NRFKeyElement?.value?.textContent as string);
}

const addUpdateNRFConfigurationMutation = useMutation<SetNrfCloudAccountMutation, SetNrfCloudAccountMutationVariables>(SetNrfCloudAccountDocument)
const addUpdateNRFConfiguration = () => {
    loadingSetNrfAccount.value = true
    return addUpdateNRFConfigurationMutation.executeMutation({
        accountId: accountId!,
        bearerToken: NRFKeyValue.value,
    }).then(({ error, data }) => {           
        if (error == null && data?.setNrfCloudAccount.bearerToken !== "") {
            //@ts-ignore
            toast(`The account is set successfully.`, {
                cardProps: {
                    color: 'success',
                },
            }) 
            onNRFEdit.value = false
        } else {
            //@ts-ignore
            toast(`Invalid credentials! Please try again.`, {
                cardProps: {
                    color: 'error',
                },
            }) 
        }
        getNRFCloudConfiguration.executeQuery({ requestPolicy: 'network-only' })
        loadingSetNrfAccount.value = false
    })
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
        name: keycloak.tokenParsed?.name,
    },
])
</script>

<style lang="scss" module>
.v-card {
    height: 80vh;
    min-width: 55vw;
    overflow-y: scroll;
    @media only screen and (max-width: 800px) {
        height: 80vh;
        width: auto;
        overflow-y: scroll;
    }
    @media only screen and (max-width: 400px) {
        height: 82vh;
        width: auto;
        overflow-y: scroll;
    }
}
.sensoscope-info-container{
    flex-grow: 2;
    @media only screen and (max-width: 600px) {
        flex-grow: 0;
    }
}

.small-text {
    @media only screen and (max-width: 1200px) {
        font-size: 10px;
    }
}
.sub-title {
    @media only screen and (max-width: 1200px) {
        font-size: 12px;
    }
}

.client-toolbar-items {
    @media only screen and (max-width: 600px) {
    margin-right: 20px;
    margin-bottom: 90px;
    margin-left: 15px;
    margin-top: -90px;
    }
}
.user-account-img{
    width: 11vw;
    @media only screen and (max-width: 800px) {
        font-size: 50px;
        width: 13vw;
    }
    @media only screen and (max-width: 600px) {
        font-size: 50px;
        width: 16vw;

    }
    @media only screen and (max-width: 400px) {
        font-size: 50px;
        width: 20vw;

    }
}

.user-account-title {
    margin-bottom: 40px;
    @media only screen and (max-width: 800px) {
        white-space: normal;
    }
    @media only screen and (max-width: 500px) {
        white-space: normal;
        margin-bottom: 10px;

    }
}

.content-wrapper {
    height: 100dvh;
    padding: 0 24px;
    background-image: url("@/assets/images/background.png");
    background-position: center;
    background-size: cover;
    @media only screen and (max-width: 600px) {
        width: 97vw;
        padding: 0 8px;
        margin-top: 90px;
        margin-left:0px;
        margin-right: 5px;
        overflow: visible;
        height: 85dvh;
    }
}
.tooltip-container {
    .tooltip-text {
        visibility: hidden;
        font-size: 12px;
        margin-bottom: 45px;
        margin-right: 10px;
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