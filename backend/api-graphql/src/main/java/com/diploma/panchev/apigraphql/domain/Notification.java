package com.diploma.panchev.apigraphql.domain;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Notification {
    private String id;
    private String deviceId;
    private String groupId;
    private NotificationThreshold threshold;
    private OffsetDateTime seenAt;
    private OffsetDateTime when;
}
