import { Client, createClient, fetchExchange, subscriptionExchange } from "@urql/vue"
import { cacheExchange } from "@urql/exchange-graphcache"
import { authExchange, type AuthUtilities } from "@urql/exchange-auth"
import { relayPagination } from "@urql/exchange-graphcache/extras"
import { createClient as createWsClient } from "graphql-ws"
import Keycloak from "keycloak-js"
import { GetDeviceGroupsDocument } from "@/generated/graphql"
import type { AssignDeviceToGroupMutation, AssignDeviceToGroupMutationVariables, GetDeviceGroupsQuery } from "@/generated/graphql"

const wsClient = createWsClient({
    url: `ws://localhost:8080/graphql`,
})
export const createUrqlClient = (keycloak?: Keycloak): Client => {
    return createClient({
        url: `http://localhost:8080/graphql`,
        exchanges: [
            cacheExchange({
                keys: {
                    Threshold: () => null,
                },
                resolvers: {
                    Account: {
                        decices: relayPagination(),
                    },
                    DeviceMeasurement: {
                        history: relayPagination(),
                    },
                },
                updates: {
                    Mutation: {
                        assignDeviceDeviceGroup: (result: AssignDeviceToGroupMutation, args: AssignDeviceToGroupMutationVariables, cache, _info) => {
                            const deviceGroupDestination = result.assignDeviceDeviceGroup
                            if (deviceGroupDestination) {
                                cache.updateQuery({
                                    query: GetDeviceGroupsDocument,
                                    variables: {
                                        accountId: args.accountId,
                                    },
                                }, (data: GetDeviceGroupsQuery | null) => {
                                //update srcGroup
                                    const deviceGroups = data?.account.deviceGroups
                                    if (deviceGroups == null) {
                                        console.warn("assignDeviceDeviceGroup cache returns null deviceGroups")
                                        return data
                                    }
                                    const sourceGroup = deviceGroups.find((deviceGroup) => {
                                        const device = deviceGroup.devices?.find((device) => device.id === args.deviceId)
                                        return device !== undefined && deviceGroup.id !== deviceGroupDestination.id
                                    })
                                    if (sourceGroup?.devices == null)
                                        return data
                                    const movedDeviceIdxInSrcGroup = sourceGroup.devices.findIndex(device => device.id === args.deviceId)
                                    sourceGroup.devices = [ ...sourceGroup.devices.slice(0, movedDeviceIdxInSrcGroup), ...sourceGroup.devices.slice(movedDeviceIdxInSrcGroup + 1) ]
                                    return data
                                })
                                
                            }
                        },
                    },
                },
            }),
            authExchange(async (utils: AuthUtilities) => {
                const token = keycloak?.token

                return {
                    addAuthToOperation(operation) {
                        if (!token) return operation
                        return utils.appendHeaders(operation, {
                            Authorization: `Bearer ${token}`,
                        })
                    },
                    didAuthError(error, operation) {
                        return error.response?.status === 401
                    },
                    async refreshAuth() {
                        keycloak?.logout()
                    },
                }
            }),
            fetchExchange,
            subscriptionExchange({
                forwardSubscription(request) {
                    const input = { ...request, query: request.query ?? "" }
                    return {
                        subscribe(observer) {
                            const unsubscribe = wsClient.subscribe(input, observer)
                            return { unsubscribe }
                        },
                    }
                },
            }),
        ],
    })
}