import { ref, computed, watchEffect, watch } from "vue"

import { useMutation } from "@urql/vue"

import { useDeviceGroupsInfo } from "@/composables/useDeviceGroups"
import { useUserStore } from "@/stores/UserStore"

import { CreateSubscriptionTokenDocument, type SubscriptionSession } from "@/generated/graphql"
import type { CreateSubscriptionTokenMutation, CreateSubscriptionTokenMutationVariables } from "@/generated/graphql"

type SubscriptionSessionPerDeviceGroup = Record<string, SubscriptionSession>
const subscriptionSession = ref<SubscriptionSessionPerDeviceGroup>({})

export const useSubscriptionToken = () => {
    const { accountId } =  useUserStore()
    const { selectedDeviceGroupInfo } = useDeviceGroupsInfo()

    const fetchToken = () => {
        if (selectedDeviceGroupInfo.value === undefined || subscriptionSession.value?.[selectedDeviceGroupInfo.value.id] !== undefined)
            return
        subscriptionTokenMutation.executeMutation({
            accountId: accountId!,
            deviceGroupId: selectedDeviceGroupInfo.value.id,
        })
    }

    const subscriptionToken = computed<string | undefined>(() => {
        if (subscriptionSession.value == null || selectedDeviceGroupInfo.value === undefined || subscriptionSession.value[selectedDeviceGroupInfo.value.id] === undefined)
            return undefined
        if ((new Date(subscriptionSession.value[selectedDeviceGroupInfo.value.id].expiresAt)).getTime() <= (new Date()).getTime()) {
            fetchToken()
            return undefined
        }

        return subscriptionSession.value[selectedDeviceGroupInfo.value.id].token
    })

    const subscriptionTokenMutation = useMutation<CreateSubscriptionTokenMutation, CreateSubscriptionTokenMutationVariables>(CreateSubscriptionTokenDocument)
    watchEffect(fetchToken)
    watchEffect(() => {
        const session = subscriptionTokenMutation.data.value?.createDeviceGroupSubscription
        if (session)
            subscriptionSession.value[selectedDeviceGroupInfo.value!.id] = session
    })
    watch(subscriptionSession, (newv) => {
        console.log("new SS", newv)
    })

    return subscriptionToken
}
