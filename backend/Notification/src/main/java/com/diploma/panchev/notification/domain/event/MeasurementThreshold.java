package com.diploma.panchev.notification.domain.event;

import com.diploma.panchev.notification.domain.measurement.MeasurementType;
import lombok.Data;

@Data
public class MeasurementThreshold {
    private String id;
    private String thresholdName;

    private MeasurementType type;
    private Double value;

    private ThresholdOperator operator;
    private Double threshold;
}
