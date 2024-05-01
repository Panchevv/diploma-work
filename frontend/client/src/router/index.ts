import { createRouter as createVueRouter, createWebHistory } from "vue-router"
// import AccountSelectionView from "@/views/AccountSelectionView.vue"
// import AccountCreationView from "@/views/AccountCreationView.vue"
// import BeehivesView from "@/views/BeehivesView.vue"
// import GeofenceView from "@/views/GeofenceView.vue"
// import LiveView from "@/views/LiveView.vue"
// import ReportsView from "@/views/ReportsView.vue"
// import DashboardView from "@/views/DashboardView.vue"
// import NotificationsView from "@/views/NotificationsView.vue"
// import IoTSimcardsView from "@/views/IoTSimcardsView.vue"
// import IoTSimcardsAddClientSecretView from "@/views/IoTSimcardsAddClientSecretView.vue"
// import AgentsView from "@/views/AgentsView.vue"
// import AgentDetailsView from "@/views/AgentDetailsView.vue"
// import InvitationsView from "@/views/InvitationsView.vue"
import { type App } from "vue"
import { useUserStore } from "@/stores/UserStore"
// import TaskManagerView from "@/views/TaskManagerView.vue"

const createRouter = (app: App) => {
    const router = createVueRouter({
        history: createWebHistory(import.meta.env.BASE_URL),
        routes: [
            // {
            //     path: "/",
            //     name: "dashboard",
            //     component: DashboardView,
            // },
            // {
            //     path: "/groups",
            //     name: "beehives",
            //     component: BeehivesView,
            // },
            // {
            //     path: "/geofences",
            //     name: "geofences",
            //     component: GeofenceView,
            // },
            // {
            //     path: "/live",
            //     name: "live",
            //     component: LiveView,
            // },
            // {
            //     path: "/history/:id?/:groupId?/:selectedTab?/:measurementType?",
            //     name: "history",
            //     component: ReportsView,
            // },
            // {
            //     path: "/notifications/:id?/:name?/:fromBeehive?",
            //     name: "notifications",
            //     component: NotificationsView,
            // },
            // {
            //     path: "/account-create",
            //     name: "accountCreation",
            //     component: AccountCreationView,
            //     meta: {
            //         hideNavbar: true,
            //     },
            // },
            // {
            //     path: "/account-select",
            //     name: "accountSelection",
            //     component: AccountSelectionView,
            //     meta: {
            //         hideNavbar: true,
            //     },
            // },
            // {
            //     path: "/iot/simcards/client",
            //     name: "simCards-secret",
            //     component: IoTSimcardsAddClientSecretView,
            // },
            // {
            //     path: "/iot/simcards/:id?",
            //     name: "simCards",
            //     component: IoTSimcardsView,
            // },
            // {
            //     path: "/smartagents",
            //     name: "smartagents",
            //     component: AgentsView,
            // },
            // {
            //     path: "/smartagent/:smartagentid",
            //     name: "smartagent",
            //     component: AgentDetailsView,
            // },
            // {
            //     path: "/invitations",
            //     name: "invitations",
            //     component: InvitationsView,
            // },
            // {
            //     path: "/taskmanager",
            //     name: "taskmanager",
            //     component: TaskManagerView,
            // },
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
