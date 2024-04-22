package com.diploma.panchev.measurement.service;

import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.Threshold;
import com.diploma.panchev.measurement.domain.ThresholdHistory;
import com.diploma.panchev.measurement.domain.ThresholdRequest;

import java.util.List;
import java.util.UUID;

public interface ThresholdService {
    Threshold createThreshold(ThresholdRequest request);

    Threshold editThreshold(UUID id, ThresholdRequest request);

    Threshold removeThreshold(UUID id);

    List<Threshold> getThresholds(String accountId);

    List<ThresholdHistory> evaluateDeviceMeasurement(DeviceMeasurement measurement);
}
