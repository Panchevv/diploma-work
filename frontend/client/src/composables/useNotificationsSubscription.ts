import { useSubscription } from "@urql/vue";
import { useSubscriptionToken } from "./useSubscriptionToken"
import { SubscribeNotificationsDocument, type SubscribeNotificationsSubscription, type SubscribeNotificationsSubscriptionVariables } from "@/generated/graphql";
import { computed, watchEffect } from "vue";
import { computedWithPrevious } from "@/utils";

export const useNotificationSubscription = () => {

    const subscriptionToken = useSubscriptionToken();
    const notificationsSubscription = useSubscription<SubscribeNotificationsSubscription, SubscribeNotificationsSubscription, SubscribeNotificationsSubscriptionVariables>({
        query: SubscribeNotificationsDocument,
        variables: computed(() => ({
            token: subscriptionToken.value!,
        })),
        pause: computed(() => subscriptionToken.value === undefined),
    });

    const liveNotifications = computedWithPrevious<SubscribeNotificationsSubscription['notifications']>((previous) => {
        const measurements = notificationsSubscription.data.value?.notifications ?? [];
        if (previous === undefined) {
            return measurements;
        }
        return previous.concat(measurements);
    });

    watchEffect(() => {
        console.log("Data: ", notificationsSubscription.data.value, "Fetching: ", notificationsSubscription.fetching.value, "Stale: ", notificationsSubscription.stale.value, "Is Paused: ", notificationsSubscription.isPaused.value);
        if (notificationsSubscription.data.value) {
            // toast(notificationsSubscription.data.value.notifications)
        }
        notificationsSubscription.data.value = undefined;
    })

    return { notificationsSubscription, liveNotifications };
}
