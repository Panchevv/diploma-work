<template>
<header :class="$style.header">
    <h1 v-if="showHeading" :class="$style.heading">
        <Logo alt="Company logo" />
    </h1>
    
    <div :class="$style.toolbar">
        <component :is="getToolbarItemType(toolbarItem)" v-for="toolbarItem in toolbarItems.slice().reverse()" :key="toolbarItem.id"
                   v-bind="isCustomToolbarItem(toolbarItem) ? toolbarItem.component.props : undefined"
                   :class="{ 'v-btn--icon': toolbarItem.type === ToolbarItemType.BUTTON }" :color="getToolbarItemColor(toolbarItem)"
                   v-on="isBasicToolbarItem(toolbarItem) ? { 'click': toolbarItem.onClick } : {}">
            <template v-if="isBasicToolbarItem(toolbarItem) && toolbarItem.type === ToolbarItemType.BUTTON">
                <div class="d-flex align-center">
                    <span v-if="toolbarItem.name">
                        {{ toolbarItem.name }}
                    </span>
                    <template v-if="toolbarItem.icon">
                        <v-progress-circular v-if="toolbarItem.loading" :class="`on-${getToolbarItemColor(toolbarItem)}`" />
                        <SvgIcon v-else v-bind="toolbarItem.icon" :class="`icon-color--on-${getToolbarItemColor(toolbarItem)}`" />
                    </template>
                </div>
            </template>
            <template v-if="isBasicToolbarItem(toolbarItem) && toolbarItem.type === ToolbarItemType.DROPDOWN" #activator="{ props: activatorProps }">
                <TheHeaderMenuActivator :activator-props="activatorProps" :loading="toolbarItem.loading"
                                        :activator-text="toolbarItem.name" :activator-icon="toolbarItem.icon" :activator-color="toolbarItem.color ?? 'primary-darken-2'" />
            </template>
            <TheHeaderMenuList v-if="isBasicToolbarItem(toolbarItem) && toolbarItem.type === ToolbarItemType.DROPDOWN" :items="toolbarItem.items!" />
        </component>
    </div>
</header>
</template>
    
<script lang="ts">
export enum ToolbarItemType {
    BUTTON,
    DROPDOWN,
    CUSTOM,
}
interface BaseToolbarItem {
    type: ToolbarItemType,
    id: string | number,
}
export interface BasicToolbarItem extends BaseToolbarItem {
    icon: SvgIconProps,
    color?: string,
    name?: string,
    items?: Array<DropdownItem | DropdownMenuItem>
    loading?: boolean
    onClick?: () => void
}
export interface CustomToolbarItem extends BaseToolbarItem {
    component: {
        component: Component,
        props: Record<string, unknown>,
    },
}
export type ToolbarItem = BasicToolbarItem | CustomToolbarItem
export enum DropdownItemType {
    BUTTON,
    MENU,
}
export interface DropdownItem {
    type: DropdownItemType
    id: number | string,
    name: string
    onClick?: () => void
}
export interface DropdownMenuItem extends DropdownItem {
    items: Array<DropdownItem>
}
const isBasicToolbarItem = (toolbarItem: BaseToolbarItem) : toolbarItem is BasicToolbarItem => toolbarItem.type !== ToolbarItemType.CUSTOM
const isCustomToolbarItem = (toolbarItem: BaseToolbarItem) : toolbarItem is CustomToolbarItem => toolbarItem.type === ToolbarItemType.CUSTOM
</script>
    
<script setup lang="ts">
import type { Component } from "vue"
    
import { VBtn, VMenu } from "vuetify/components"
import SvgIcon, { type SvgIconProps } from "@/components/SvgIcon.vue"
import TheHeaderMenuActivator from "@/components/TheHeaderMenuActivator.vue"
import TheHeaderMenuList from "@/components/TheHeaderMenuList.vue"
    
import Logo from "@/assets/images/logo_2.png"
    
const props = defineProps<{
    showHeading?: boolean,
    toolbarItems: Array<ToolbarItem>,
}>()
const getToolbarItemType = (toolbarItem: BaseToolbarItem) => {
    switch (toolbarItem.type) {
        case ToolbarItemType.DROPDOWN:
            return VMenu
        case ToolbarItemType.BUTTON:
            return VBtn
        case ToolbarItemType.CUSTOM:
            return (toolbarItem as CustomToolbarItem).component.component
    }
}
const getToolbarItemColor = (toolbarItem: BaseToolbarItem) =>
    toolbarItem.type === ToolbarItemType.BUTTON && isBasicToolbarItem(toolbarItem) ?
        (toolbarItem.color ?? "primary-darken-2") :
        undefined
</script>
    
<style lang="scss" module>
.header {
    position: relative;
    z-index: 1000;
    @media only screen and (max-width: 600px) { 
        z-index: 999;
    }
    .heading,.toolbar {
        padding: 1.5rem 0 2.5rem;

            @media only screen and (max-width: 600px) { 
                display: flex;
                flex-direction: row;
            }
    }
    .heading {
        @media only screen and (max-width: 600px) {  
            display: none;
            }
    }

    .toolbar {
        position: absolute;
        top: 0;
        right: 0;
        display: flex;
        gap: .5rem;
        align-items: flex-start;
        margin-top: -0.25rem;
        
        @media only screen and (max-width: 600px) { 
            align-items: flex-end;
            margin-left: 20%; 
            margin-right: -15px;
            margin-top: -1rem; 
            gap: 5px; 
            };

    }
}
</style>
    