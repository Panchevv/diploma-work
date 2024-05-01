<template>
<nav :class="pickCssClass(`sidebar ${!modelValue ? 'close' : ''}`, $style)">
    <header>
        <button v-ripple type="button" :class="pickCssClass('toggle nav-link icon-wrapper', $style)" @click="toggleSidebar">
            <SvgIcon v-if="modelValue" :type="IconType.FONT_AWESOME" category="fas" name="xmark" alt="" :class="pickCssClass('icon', $style)" />
            <SvgIcon v-else :type="IconType.FONT_AWESOME" category="fas" name="bars" alt="" :class="pickCssClass('icon', $style)" />
        </button>
        <div :class="pickCssClass('company-logo', $style)">
            <img src="@/assets/images/logo.png" alt="Menu logo" />
        </div>
    </header>
    <div :class="pickCssClass('menu-bar', $style)">
        <div :class="pickCssClass('menu', $style)">
            <ul :class="pickCssClass('menu-links', $style)">
                <li v-for="navItem in navItems" :key="navItem.name" v-ripple>
                    <RouterLink v-if="isNavItemRoute(navItem)" :to="navItem.to" :class="pickCssClass('nav-link', $style)">
                        <span :class="pickCssClass('nav-icon-wrapper', $style)">
                            <SvgIcon :type="navItem.icon.type" :category="navItem.icon.category" :name="navItem.icon.name" alt="" :class="pickCssClass('icon', $style)" />
                        </span>
                        <span :class="pickCssClass('nav-link__text', $style)">{{ navItem.name }}</span>
                        <span v-if="modelValue === false" :class="pickCssClass('tooltip-text', $style)">{{ navItem.name }}</span>
                    </RouterLink>
                    <button v-else v-ripple type="button" :class="pickCssClass('nav-link', $style)" @click="navItem.onClick">
                        <span :class="pickCssClass('nav-icon-wrapper', $style)">
                            <SvgIcon :type="navItem.icon.type" :category="navItem.icon.category" :name="navItem.icon.name" alt="" :class="pickCssClass('icon', $style)" />
                        </span>
                        <span :class="pickCssClass('nav-link__text', $style)">{{ navItem.name }}</span>
                        <span v-if="modelValue === false" :class="pickCssClass('tooltip-text', $style)">{{ navItem.name }}</span>
                    </button>
                </li>
            </ul>
        </div>
    </div>
</nav>
</template>

<script lang="ts">
import { IconType } from "@/components/SvgIcon.vue"
import { useUserStore } from "@/stores/UserStore"

interface BaseNavItem {
    name: string,
    icon: {
        type: IconType,
        category?: string,
        name: string
    },
}
interface NavItemRoute extends BaseNavItem {
    to: string,
}
interface NavItemButton extends BaseNavItem {
    onClick: () => void,
}
type NavItem = NavItemButton | NavItemRoute

const isNavItemButton = (navItem: NavItem): navItem is NavItemButton => 'onClick' in navItem
const isNavItemRoute = (navItem: NavItem): navItem is NavItemRoute => 'to' in navItem
</script>

<script setup lang="ts">
import { ref } from "vue"

import { RouterLink, useRouter } from "vue-router"

import SvgIcon from "@/components/SvgIcon.vue"

import { pickCssClass } from "@/utils"

const { logout } = useUserStore()

const modelValue = defineModel<boolean>()

const toggleSidebar = () => {
    modelValue.value = !modelValue.value
}
const router = useRouter();

const closeSidebar = () => {
    if (window.innerWidth <= 600) {
        modelValue.value = false
    }
}

const navItems = ref<Array<NavItem>>([
    {
        name: "nRF Cloud",
        icon: {
            type: IconType.MATERIAL,
            category: "filled",
            name: "manage_accounts",
        },
        onClick: () => { 
            router.push("/")
            closeSidebar(); 
        },
    },
    {
        name: "Sensors",
        icon: {
            type: IconType.MATERIAL,
            category: "filled",
            name: "sensors",
        },
        onClick: () => { 
            router.push("/groups")
            closeSidebar(); 
        },
    },
    {
        name: "Thresholds",
        icon: {
            type: IconType.NORMAL,
            name: "icon_smart_agent",
        },
        onClick: () => { 
            router.push("/thresholds")
            closeSidebar(); 
        },
    },
    {
        name: "Reports",
        icon: {
            type: IconType.MATERIAL,
            category: "filled",
            name: "history",
        },
        onClick: () => { 
            router.push("/history")
            closeSidebar(); 
        },
    },
    {
        name: "Logout",
        icon: {
            type: IconType.MATERIAL,
            category: "filled",
            name: "logout",
        },
        onClick: () => logout(),
    },
])
</script>

<style lang="scss" module>
$tran-02: all 0.2s ease;
$tran-03: all 0.3s ease;
$tran-04: all 0.4s ease;
$tran-05: all 0.5s ease;
$tran-opacity: opacity 1s ease;
$menu-items-color: rgba(var(--v-theme-on-primary));

.sidebar {
    height: 100%;
    width: 200px;
    z-index: 402;
    background-color: rgba(var(--v-theme-primary));
    transition: $tran-05;
    overflow: hidden;

    @media only screen and (max-width: 600px) {
        top: 0;
        left: 0;
        height: 520px;
        width: 100vw;
        z-index: 800;  
        background-color: rgba(var(--v-theme-primary));
        transition: $tran-05;
        overflow: hidden;
        border-bottom-left-radius: 5%;
        border-bottom-right-radius: 5%;
    }

    .nav-link {
        display: flex;
        align-items: center;
        width: 100%;
        min-height: 50px;
        list-style: none;
        background-color: transparent;
        border-radius: 6px;
        text-decoration: none;

        &__text {
            font-size: .875rem;
            font-weight: 500;
            white-space: nowrap;
            color: $menu-items-color;
        }

        &.toggle {
            width: unset;
        }

        &:hover {
            background-color: rgba($color: #000000, $alpha: 0.04);
        }
    }

    &.close {
        width: 70px;
        @media only screen and (max-width: 600px) {
        height: 62px;
        width: 100vw;
        }
    }

    li {
        list-style: none;
    }

    header {
        display: flex;
        position: relative;

        .toggle {
            flex: 0 0 70px;
        }

        .company-logo {
            flex: 1 1 auto;
            display: flex;
            align-items: center;
            justify-items: flex-start;
            justify-content: flex-start;
            img {
                max-width: 100px;
                @media only screen and (max-width: 600px) {
                    max-width: 160px;
                    margin-top: 70px;
                    margin-left: -50px;
                }
            }
        }

    }
    .nav-icon-wrapper {
        display: flex;
        justify-content: center;
        min-width: 70px;
        .icon {
            color: $menu-items-color;
            fill: $menu-items-color;
        }
    }

    .icon-wrapper {
        display: flex;
        justify-content: center;
        min-width: 70px;
        @media only screen and (max-width: 600px) {
            margin-top: -33px;
        }
        .icon {
            color: $menu-items-color;
            fill: $menu-items-color;
        }
    }

    .menu {
        margin-top: 40px;
    }

    .menu-bar {
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .menu-links {
            padding: 0;
        }
    }
}

li {
    .tooltip-text {
        visibility: hidden;
        font-size: 12px;
        margin-left: 65px;
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
            @media (hover: none){
            visibility: hidden; 
        }
        }
    }
}
</style>