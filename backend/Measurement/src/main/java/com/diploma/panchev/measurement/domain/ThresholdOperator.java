package com.diploma.panchev.measurement.domain;

import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.function.BiFunction;

@AllArgsConstructor
public enum ThresholdOperator {
    EQUAL(Objects::equals),
    LESS_THAN((value, threshold) -> value < threshold),
    GREATER_THAN((value, threshold) -> value > threshold);


    private final BiFunction<Double, Double, Boolean> evaluation;

    public boolean evaluate(double value, double threshold) {
        return this.evaluation.apply(value, threshold);
    }
}
