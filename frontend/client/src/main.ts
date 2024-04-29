import { createApp, h } from "vue"

import { createPinia } from "pinia"
import piniaPluginPersistedstate from "pinia-plugin-persistedstate"
import { createVuetify, type IconAliases, type IconProps, type IconSet } from "vuetify"
// @ts-ignore
import colors from "vuetify/lib/util/colors"

import { vShow } from "@/directives/vShow"

import App from "@/App.vue"
import SvgIcon, { IconType } from "@/components/SvgIcon.vue"
import { fontAwesomeIcons, materialIcons, type MaterialIconsType } from "@/components/iconComponents"

import { library } from "@fortawesome/fontawesome-svg-core"
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome"
import "@mdi/font/css/materialdesignicons.css"
import { aliases, mdi } from "vuetify/iconsets/mdi"
import "@/assets/styles/main.scss"

import createRouter from "@/router"
import { initKeycloak } from "@/keycloak"

import theTheme from "@/theme.json"

const app = createApp(App).
    component("FontAwesomeIcon", FontAwesomeIcon).
    directive("cshow", vShow)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const themes = Object.keys(theTheme.themes).reduce((result: typeof theTheme.themes, themeName: string) => {
    const theme = theTheme.themes[themeName as keyof typeof theTheme.themes]
    const themeObject = { ...theme }
    themeObject.colors = Object.keys(theme.colors).reduce((accumulator: typeof themeObject.colors, colorName: string) => {
        let color: string = theme.colors[colorName as keyof typeof theme.colors]
        if (color.startsWith("colors")) {
            const colorTokens = color.split(".")
            color = colors[colorTokens[1] as keyof typeof themeObject.colors][colorTokens[2]]
        }
        accumulator[colorName as keyof typeof accumulator] = color
        return accumulator
    }, themeObject.colors)

    result[themeName as keyof typeof themes] = themeObject
    return result
}, theTheme.themes)

const iconAliases: IconAliases = Object.keys(materialIcons).reduce((accumulator: IconAliases, category: string) => {
    const iconsInCategory = materialIcons[category as keyof MaterialIconsType]
    Object.keys(iconsInCategory).forEach((iconName: string) => {
        accumulator[`${category}/${iconName}`] = iconsInCategory[iconName]
    })
    return accumulator
}, {} as IconAliases)

const materialIconsSet: IconSet = {
    component: (props: IconProps) => h(SvgIcon, {
        type: IconType.MATERIAL,
        category: (props.icon as string).split("/")[0],
        name: (props.icon as string).split("/")[1],
        alt: "",
    }),
}

const vuetify = createVuetify({
    defaults: {
        VProgressLinear: {
            color: "primary",
            indeterminate: true,
            height: 3,
        },
        VProgressCircular: {
            color: "primary",
            indeterminate: true,
        },
    },
    theme: {
        defaultTheme: "light",
        variations: {
            colors: [ 'primary' ],
            lighten: 5,
            darken: 5,
        },
        themes,
    },
    icons: {
        defaultSet: "mdi",
        aliases: { ...aliases, ...iconAliases },
        sets: {
            mdi,
            materialIconsSet,
        },
    },
})

library.add(...Object.values(fontAwesomeIcons))
app.
    use(pinia)

initKeycloak(window.location.pathname, () => {
    app.use(createRouter(app)).
        use(vuetify).
        mount("#app")
})