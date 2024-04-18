package com.diploma.panchev.apigraphql.adapter.notification;

import com.diploma.panchev.apigraphql.domain.SubscriptionSession;

public interface NotificationAdapter {
    SubscriptionSession createStream(String accountId, String groupId);
}
