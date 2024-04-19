package com.diploma.panchev.apigraphql.adapter.notification;

import com.diploma.panchev.apigraphql.domain.Notification;
import com.diploma.panchev.apigraphql.domain.SubscriptionSession;

import java.util.List;

public interface NotificationAdapter {
    SubscriptionSession createStream(String accountId, String groupId);

    List<Notification> getNotificationHistory(String groupId, Integer pageSize, String last, String deviceId);
}
