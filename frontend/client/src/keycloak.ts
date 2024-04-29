import Keycloak, { type KeycloakInitOptions } from "keycloak-js"
import { useUserStore } from "./stores/UserStore"

// const config: KeycloakConfig = {
//     clientId: "web-client",
//     realm: "diploma",
//     url: "http://localhost:8085/",
// }
const keycloak: Keycloak = new Keycloak('/keycloak.json')
const keycloakInitOptions: KeycloakInitOptions = {
    onLoad: "check-sso", silentCheckSsoRedirectUri: `${window.location.origin}/silent-check-sso.html`, checkLoginIframe: false,
    flow: "implicit", redirectUri: `${window.location.origin}/`,
}

export const initKeycloak = (currentRoute: string, callback: () => void) => {
    const { setKeycloak } = useUserStore()
    if (currentRoute !== "/")
        keycloakInitOptions.redirectUri += currentRoute
    keycloak.init(keycloakInitOptions).
        then((authenticated: boolean) => {
            if (!authenticated) {
                keycloak.login()
            } else {
                setKeycloak(keycloak)
                callback()
            }
        }).
        catch(error => console.error(error))
}
