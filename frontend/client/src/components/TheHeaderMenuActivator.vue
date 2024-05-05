<template>
<div :class="$style.activator">
    <span v-if="activatorText" :class="$style['activator-text']">{{ activatorText }}</span>
    <v-btn v-bind="activatorProps" :color="activatorColorName" class="v-btn--icon">
        <template v-if="activatorIcon">
            <v-progress-circular v-if="loading" :class="loadingInficatorColorClassName" />
            <SvgIcon v-else v-bind="activatorIcon" :class="iconColorClassName" />
        </template>
    </v-btn>
</div>
</template>
    
<script setup lang="ts">
import { computed } from "vue"
    
import SvgIcon  from "@/components/SvgIcon.vue"
    
const props = defineProps<{
    activatorProps: any,
    activatorText?: string,
    activatorColor?: string,
    textBgColor?: string,
    activatorIcon?: any,
    loading?: boolean
}>()
const activatorColorName = computed(() => props.activatorColor != null ?
    (props.textBgColor != null ? props.activatorColor : `${props.activatorColor}-darken-2`) :
    "primary-darken-2")
const textBgColorName = computed(() => props.textBgColor ?? props.activatorColor ?? "primary")
const textBgColorValue = computed(() => `rgb(var(--v-theme-${textBgColorName.value}))`)
const textColorValue = computed(() => `rgb(var(--v-theme-on-${textBgColorName.value}))`)
const iconColorClassName = computed(() => `icon-color--on-${activatorColorName.value}`)
const loadingInficatorColorClassName = computed(() => `on-${activatorColorName.value}`)
</script>

<style lang="scss" module>
.activator {
    display: flex;
    align-items: center;
}

.activator-text {
    margin-right: -10px;
    padding: 4px 10px;
    border-radius: 24px 0 0 24px;
    background-color: v-bind(textBgColorValue);
    color: v-bind(textColorValue);
    font-size: .875rem;
    font-weight: bold;
    @media only screen and (max-width: 800px) { 
        display: none;
        };
}
</style>
