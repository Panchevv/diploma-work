import { defineStore } from "pinia"
import Keycloak from "keycloak-js"

export type UserStoreState = {
    keycloak: Keycloak
    accountId?: string
    accountName?: string
    subscriptionToken?: string
}
export const useUserStore = defineStore("userStore", {
    state: () => ({
        /**
         * Only for debugging purposes. Use the getters and actions of the UserStore
         */
        keycloak: undefined as unknown as Keycloak,
        accountId: undefined,
        accountName: undefined,
        subscriptionToken: undefined,
    } as UserStoreState),
    getters: {
        authenticated: (state: UserStoreState) => !!state.keycloak?.authenticated,
        isTokenExpired: (state: UserStoreState) => !!state.keycloak?.isTokenExpired,
        token: (state: UserStoreState) => state.keycloak?.token,
        tokenParsed: (state: UserStoreState) => state.keycloak?.tokenParsed,
        realm: (state: UserStoreState) => state.keycloak?.realm,
        idToken: (state: UserStoreState) => state.keycloak?.idToken,
        idTokenParsed: (state: UserStoreState) => state.keycloak?.idTokenParsed,
        clientId: (state: UserStoreState) => state.keycloak?.clientId,
        profile: (state: UserStoreState) => state.keycloak?.profile,
    },
    actions: {
        setKeycloak(keycloak: Keycloak) {
            this.keycloak = keycloak
        },
        setAccountId(accountId: string) {
            this.accountId = accountId
        },
        setAccountName(accountName: string) {
            this.accountName = accountName
        },
        setSubscriptionToken(token: string) {
            this.subscriptionToken = token
        },
        login() {
            this.keycloak.login()
        },
        logout() {
            this.accountId = undefined
            this.accountName = undefined
            this.keycloak.logout()
        },
    },
    persist: {
        storage: localStorage,
        paths: [ 'accountId', 'accountName' ],
    },
})
