package com.diploma.panchev.apigraphql.adapter.measurement;

import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;

import java.util.List;

public interface MeasurementAdapter {
    Threshold createThreshold(String accountId, ThresholdNeedle threshold);

    Threshold editThreshold(String id, ThresholdNeedle threshold);

    Threshold deleteThreshold(String id);

    List<Threshold> getThresholds(String accountId);
}
