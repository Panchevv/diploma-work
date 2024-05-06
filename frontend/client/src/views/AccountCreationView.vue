<template>
<main :class="$style['content-wrapper']">
    <div :class="$style['account-create-box']">
        <h2 :class="$style.heading">Create an account</h2>
        <v-card :class="$style['account-list']">
            <div class="d-flex flex-column">
                <v-text-field v-model="accountName" placeholder="Acount Name" style="width: 200px;" variant="underlined" hide-details>
                    <SvgIcon class="mx-3" :class="extractCssClass('person-icon', $style)" :type="IconType.MATERIAL" category="round" name="person_add" alt="person_add_icon" />
                </v-text-field>
            </div>
        </v-card>
        <div class="d-flex">
            <v-btn variant="text" class="ma-1" :class="extractCssClass('action-btn', $style)" @click="redirectBack()">
                <span :class="extractCssClass('action-btn-text', $style)">Cancel</span>
            </v-btn>
            <v-btn :disabled="!accountName" variant="text" class="ma-1" :class="extractCssClass('action-btn', $style)" @click="createAccount()">
                <span :class="extractCssClass('action-btn-text', $style)">Save</span>
            </v-btn>
        </div>
    </div>
</main>
</template>
    
<script setup lang="ts">
import { ref } from "vue";

import { useRouter } from "vue-router";
import { useMutation } from "@urql/vue";

import type { CreateAccountMutation, CreateAccountMutationVariables } from "@/generated/graphql"
import  { CreateAccountDocument } from "@/generated/graphql"
    
import SvgIcon, { IconType } from "@/components/SvgIcon.vue";
import { extractCssClass } from "@/utils";

const router = useRouter()
const accountName = ref("")

const redirectBack = () => {
    router.push('/account-select')
}
const saveAccount = () => {
    router.push('/account-select')
}

const createAccountMutation = useMutation<CreateAccountMutation, CreateAccountMutationVariables>(CreateAccountDocument)
const createAccount = () => {
    return createAccountMutation.executeMutation({
        name: accountName.value,
    }).then(({ error }) => {
        if (error == null) {
            saveAccount()
        }
    })
}
</script>

<style lang="scss" module>
@import "../assets/styles/functions";

.content-wrapper {
    width: 100vw;
    height: 100vh;
    display: grid;
    align-items: center;
    justify-items: center;

    .account-create-box {
        height: 30dvh;
        max-height: 600px;
        display: flex;
        flex-direction: column;
        width: 30vw;
        align-items: center;
        justify-content: space-between;
        padding: 2rem 4rem;
        background-color: rgb(var(--v-theme-on-secondary));
        box-shadow: rgba(17, 12, 46, 0.15) 0px 48px 100px 0px;
        border-radius: 1rem;
        max-width: 500px;

        .heading {
            color: rgb(var(--v-theme-primary))
        }

        .account-list {
            margin-top: 16px;
        }
    }

    background-image: url("@/assets/images/background.png");
    background-position: center;
    background-size: cover;

    @media screen and (max-width: 1280px) {
        .account-create-box {
            padding: 1rem 2rem;
            width: 50vw;
        }
    }

    @media screen and (max-width: 960px) {
        .account-create-box {
            width: 50vw;
        }
    }

    @media screen and (max-width: 600px) {
        .account-create-box {
            height: 30vh; 
            width: 80vw;
        }

        .action-btn {
            width: 50%;
            margin-top: 8px;
        }
    }
}

.person-icon {
    fill: rgb(var(--v-theme-on-background));
}

.action-btn {
    width: 5rem;
    margin-top: 0.5rem;
}

.action-btn-text {
    color: rgb(var(--v-theme-on-background));
}
</style>
