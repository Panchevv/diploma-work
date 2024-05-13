import { createRouter as createVueRouter, createWebHistory } from "vue-router"
import AccountSelectionView from "@/views/AccountSelectionView.vue"
import AccountCreationView from "@/views/AccountCreationView.vue"
import SensorsView from "@/views/SensorsView.vue"
import ReportsView from "@/views/ReportsView.vue"
import NrfCloudView from "@/views/NrfCloudView.vue"
import NotificationsView from "@/views/NotificationsView.vue"
import ThresholdsView from "@/views/ThresholdsView.vue"
import { type App } from "vue"
import { useUserStore } from "@/stores/UserStore"

const createRouter = (app: App) => {
    const router = createVueRouter({
        history: createWebHistory(import.meta.env.BASE_URL),
        routes: [
            {
                path: "/",
                name: "nRF Cloud",
                component: NrfCloudView,
            },
            {
                path: "/groups",
                name: "sensors",
                component: SensorsView,
            },
            {
                path: "/history/:id?/:groupId?/:selectedTab?/:measurementType?",
                name: "history",
                component: ReportsView,
            },
            {
                path: "/notifications/:id?/:name?/:fromBeehive?",
                name: "notifications",
                component: NotificationsView,
            },
            {
                path: "/account-create",
                name: "accountCreation",
                component: AccountCreationView,
                meta: {
                    hideNavbar: true,
                },
            },
            {
                path: "/account-select",
                name: "accountSelection",
                component: AccountSelectionView,
                meta: {
                    hideNavbar: true,
                },
            },
            {
                path: "/thresholds",
                name: "thresholds",
                component: ThresholdsView,
            },
        ],
    })

    router.beforeEach((to) => {
        const { authenticated, accountId } = useUserStore()
        if ((to.path !== "/account-select" && to.path !== "/account-create") && authenticated && accountId === undefined)
            return { name: "accountSelection" }
    })
    return router
}

export default createRouter
