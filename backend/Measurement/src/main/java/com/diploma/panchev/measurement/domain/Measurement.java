package com.diploma.panchev.measurement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    private MeasurementType type;
    private Double value;
    private OffsetDateTime when;
}
