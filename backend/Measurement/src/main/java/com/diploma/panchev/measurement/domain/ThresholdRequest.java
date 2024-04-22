package com.diploma.panchev.measurement.domain;

import lombok.Data;

import java.util.List;

@Data
public class ThresholdRequest {
    private String name;
    private String accountId;
    private List<String> groupIds;
    private ThresholdOperator operator;
    private MeasurementType measurementType;
    private Double value;
}
