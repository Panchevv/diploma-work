package com.diploma.panchev.measurement.domain;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class DeviceMeasurement {
    private String measurementId;
    private String messageId;
    private String deviceId;
    private String groupId;
    private Measurement measurement;

    private OffsetDateTime createdAt;
}
