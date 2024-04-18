package com.diploma.panchev.apigraphql.domain;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Measurement {
    private Long id;
    private String deviceId;
    private MeasurementType type;
    private Double value;
    private OffsetDateTime when;

    public String getId() {
        return deviceId + "#" + id;
    }

    public String getCursor() {
        return deviceId + "#" + type + "#" + id;
    }
}
