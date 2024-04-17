package com.diploma.panchev.apigraphql.domain;

import lombok.Data;

import java.util.List;

@Data
public class ThresholdNeedle {
    private String name;
    private List<String> groupIds;
    private MeasurementType measurementType;
    private ThresholdOperator operator;
    private double value;
}
