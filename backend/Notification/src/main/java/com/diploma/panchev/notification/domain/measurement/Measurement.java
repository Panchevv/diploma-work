package com.diploma.panchev.notification.domain.measurement;

import lombok.Data;

@Data
public class Measurement {
    private MeasurementType type;
    private Double value;
}
