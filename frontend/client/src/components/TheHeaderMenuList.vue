<template>
<!-- v-list -> v-list-item -> template -> v-menu -><- -->
<v-list>
    <v-list-item v-for="item in items" :key="item.id" :id="`menu-activator-${item.id}`" :value="item" @click="item.onClick">
        <template v-if="item.type === DropdownItemType.MENU" #default>
            <v-menu location="start" :open-on-hover="!shouldOpenOnHover" :activator="`#menu-activator-${item.id}`">
                <TheHeaderMenuList :items="(item as DropdownMenuItem).items" />
            </v-menu>
        </template>
        <template #title>
            {{ item.name }}
        </template>
    </v-list-item>
</v-list>
</template>
    
<script setup lang="ts">
import { type DropdownItem, type DropdownMenuItem, DropdownItemType } from "./TheHeader.vue"
    
const props = defineProps<{
    items: Array<DropdownItem | DropdownMenuItem>
}>()
    
const shouldOpenOnHover = 'ontouchstart' in window || (navigator.maxTouchPoints > 0);
</script>

<style lang="scss" module>
@import "../assets/styles/functions";

.arrow-icon {
    fill: rgb(0 0 0 / 70%);
    path: {
        color: rgb(0 0 0 / 70%);
    }

    transform: scale(calc-svg-scale(14));
    
}
</style>
    