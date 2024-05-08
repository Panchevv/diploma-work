<template>
<table :class="pickCssClass(`scrollable-table ${attrs.class}`, $style)">
    <thead>
        <slot name="head"></slot>
    </thead>
    <tbody>
        <slot name="body"></slot>
    </tbody>
</table>
</template>

<script setup lang="ts">
import { useAttrs } from "vue"
import { pickCssClass } from "@/utils"

const props = defineProps<{
    width: string
    bodyHeight: string
}>()
const attrs = useAttrs()
</script>

<style lang="scss" module>
.scrollable-table {
    border-collapse: collapse;
    border-spacing: 0;

    :global {
        &,thead,tbody,tr {
            display: block;
            width: v-bind(width);
        }

        tr {
            display: table;
            table-layout: fixed;
        }

        tbody {
            height: v-bind(bodyHeight);
            overflow-x: hidden;
            overflow-y: auto;
        }
    }
}
</style>
