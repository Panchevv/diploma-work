package com.diploma.panchev.measurement.domain;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ThresholdHistory {
    private String id;
    private String deviceId;
    private String groupId;
    private Threshold threshold;
    private Measurement measurement;
    private OffsetDateTime when;
}
