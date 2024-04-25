package com.diploma.panchev.nrfcloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Measurement {
    private MeasurementType type;
    private double value;
}
