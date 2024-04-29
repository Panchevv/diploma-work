<template>
<div :class="$style['page-wrapper']">
    <TheNavigation v-if="!$route.meta.hideNavbar" v-model="navExpanded" />
    <RouterView />
    <VSonner />
</div>
</template>

<script setup lang="ts">
import { computed, ref } from "vue"

import { provideClient } from "@urql/vue"
import { RouterView } from "vue-router"

import { useUserStore } from "@/stores/UserStore"

import TheNavigation from "@/components/TheNavigation.vue"

import { createUrqlClient } from "./graphql/client"
import { VSonner } from "vuetify-sonner"

const { keycloak } = useUserStore()
provideClient(createUrqlClient(keycloak))
  
const navExpanded = ref<boolean>(false)
const navWidth = computed<string>(() => navExpanded.value ? "200px" : "70px")
</script>
  
<style lang="scss" module>
.page-wrapper {
    height: 100vh;
    height: 100dvh;
    display: grid;
    grid-template: 1fr / v-bind(navWidth) 1fr;

    /* grid-template: 1fr / 200px 1fr; */
    transition: 0.5s ease;
    @media only screen and (max-width: 600px) {
        grid-template: 1fr / v-bind(0) 1fr;
        }
}
</style>