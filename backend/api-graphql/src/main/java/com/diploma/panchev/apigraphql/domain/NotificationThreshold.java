package com.diploma.panchev.apigraphql.domain;

import lombok.Data;

@Data
public class NotificationThreshold {
    private String id;
    private String thresholdName;
    private ThresholdOperator operator;

    private MeasurementType measurementType;
    private Double measurementValue;

    private Double threshold;
}
