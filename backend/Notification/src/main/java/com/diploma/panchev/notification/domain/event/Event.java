package com.diploma.panchev.notification.domain.event;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Event {
    private String id;
    private String ruleId;
    private String deviceId;
    private String referenceId;
    private String triggeredAt;
    private String groupId;
    private String name;
    private MeasurementThreshold threshold;
    private OffsetDateTime createdAt;
}
