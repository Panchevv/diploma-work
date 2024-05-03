<template>
<main :class="$style['content-wrapper']">
    <div>
        <img src="@/assets/images/logo_2.png" alt="" style="transform: scale(0.6); border-radius: 20%;" />
    </div>
    <div :class="$style['account-select-box']">
        <h2 :class="$style.heading">
            Select an account
        </h2>
        <v-progress-linear v-if="fetching" class="mt-1" />
        <div :class="$style['account-list-container']">
            <v-card v-for="account in accounts" :key="account.id" :class="$style['account-list']" @click="selectAccount(account)">
                <div class="d-flex justify-space-between my-1 px-4 py-2">
                    {{ account.name }}
                    <SvgIcon :type="IconType.MATERIAL" category="outlined" name="chevron_right" alt="Right chevron" class="icon-black mr-n2" />
                </div>
            </v-card>
        </div>
    </div>
    <v-card :class="$style['account-list']" class="align-self-end" @click="redirectCreateAccount()">
        <div class="d-flex justify-space-between my-1 px-4 py-2">
            <span>Create an account</span>
            <SvgIcon :type="IconType.MATERIAL" category="outlined" name="chevron_right" alt="Right chevron" class="icon-black mr-n2" />
        </div>
    </v-card>
</main>
</template>
    
<script setup lang="ts">
import { computed } from "vue"
    
import { useQuery } from "@urql/vue"
import { useRouter } from "vue-router"
    
import { useUserStore } from "@/stores/UserStore"
    
import { GetAccountsDocument, type Account, type Query } from "@/generated/graphql"
    
import SvgIcon, { IconType } from "@/components/SvgIcon.vue"
    
import _ from "lodash"
import { useGlobalStore } from "@/stores/GlobalStore"
import { storeToRefs } from "pinia"
    
const { setAccountId, setAccountName } = useUserStore()
const router = useRouter()
const { selectedDeviceGroupId } = storeToRefs(useGlobalStore())
    
const result = useQuery<Query>({
    query: GetAccountsDocument,
    context: {
        requestPolicy: "network-only", // refetch every time: when switching to this view and when switching device groups
    },
})
const accounts = computed(() => result.data.value?.accounts)
const fetching = computed(() => result.fetching.value)
    
const selectAccount = (account: Account) => {
    setAccountId(account.id)
    setAccountName(account.name as string)
    selectedDeviceGroupId.value = undefined
    router.push('/')
}
const redirectCreateAccount = () => {
    router.push('/account-create');
}
</script>

<style lang="scss" module>
.content-wrapper {
    width: 100vw;
    height: 100vh;
    display: grid;
    align-items: center;
    justify-items: center;
    @media only screen and (max-width: 600px) {
        height: 90vh;
    }

    .account-select-box {
        height: 35dvh;
        max-height: 500px;
        display: flex;
        flex-direction: column;
        width: 90vw;
        max-width: 500px;
        align-items: center;
        padding: 2rem 4rem;
        background-color: rgb(var(--v-theme-on-secondary));
        border-radius: 1rem;
        box-shadow: rgba(17, 12, 46, 0.15) 0px 48px 100px 0px;

        .heading {
            color: rgb(var(--v-theme-primary))
        }

        .account-list-container {
            height: 80%;
            width: 70%;
            overflow-y: scroll;
        }

        .account-list {
            margin-top: 1rem;
        }
    }

    background-image: url("@/assets/images/background.png");
    background-position: center;
    background-size: cover;

    @media screen and (max-width: 1280px) {
        .account-select-box {
            padding: 1rem 2rem; 
        }
    }

    @media screen and (max-width: 960px) {
        .account-select-box {
            width: 80vw; 
        }
    }
}
</style>

