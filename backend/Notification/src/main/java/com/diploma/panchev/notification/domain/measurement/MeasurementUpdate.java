package com.diploma.panchev.notification.domain.measurement;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class MeasurementUpdate {
    private String id;
    private String deviceId;
    private String groupId;
    private Measurement measurement;
    private String messageId;
    private OffsetDateTime createdAt;
}
