package com.diploma.panchev.measurement.domain;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Threshold {
    private UUID id;
    private String accountId;
    private String name;
    private ThresholdOperator operator;
    private MeasurementType measurementType;
    private Double value;
    private OffsetDateTime modifiedAt;
    private OffsetDateTime createdAt;
    private List<String> groupIds;
}
