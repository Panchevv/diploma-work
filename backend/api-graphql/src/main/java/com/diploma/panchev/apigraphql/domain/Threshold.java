package com.diploma.panchev.apigraphql.domain;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class Threshold {
    private String id;
    private String accountId;
    private String name;
    private List<String> groupIds;
    private MeasurementType measurementType;
    private ThresholdOperator operator;
    private double value;
    private OffsetDateTime modifiedAt;
    private OffsetDateTime createdAt;
}
