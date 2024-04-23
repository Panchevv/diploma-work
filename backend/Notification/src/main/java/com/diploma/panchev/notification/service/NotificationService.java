package com.diploma.panchev.notification.service;

import com.diploma.panchev.notification.domain.event.Event;

import java.util.List;

public interface NotificationService {
    List<Event> getNotificationHistory(String groupId, String deviceId, String last, Integer pageSize);
}
