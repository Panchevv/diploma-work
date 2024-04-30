<template>
<FontAwesomeIcon v-if="type === IconType.FONT_AWESOME" :icon="[ category, name ]" :alt="alt" v-bind="$attrs" />
<component :is="materialIcons[category! as keyof MaterialIconsType][name]" v-else-if="type === IconType.MATERIAL" :alt="alt" v-bind="$attrs" />
<component :is="svgImage" v-else-if="type === IconType.NORMAL && svgImage !== undefined" :alt="alt" :viewBox="viewPortCorrections" v-bind="$attrs" />
</template>

<script lang="ts">
export enum IconType {
    FONT_AWESOME,
    MATERIAL,
    NORMAL,
}
export type SvgIconProps = {
    type: IconType,
    name: string,
    alt: string,
    category?: string,
    fixImagePadding?: boolean,
}
const iconViewPortCorrections: {[key: string]: Array<number>} = {
}
</script>

<!-- <script setup lang="ts" generic="CT extends IconType"> -->
<script setup lang="ts">
import { onMounted, ref } from "vue"
import { materialIcons, type MaterialIconsType } from "@/components/iconComponents"

defineOptions({
    inheritAttrs: false,
})
const props = defineProps<SvgIconProps>()
const svgImage = ref<any>()
const viewPortCorrections = ref<string | undefined>(props.fixImagePadding ? iconViewPortCorrections[props.name].join(" ") : undefined)
onMounted(async () => {
    if (props.type !== IconType.NORMAL)
        return
    svgImage.value = await import(`../assets/images/icons/${props.name}.svg?component`)
})
</script>

<style scoped>

</style>
    