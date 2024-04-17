package com.diploma.panchev.apigraphql.service;

import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;

public interface MeasurementService {
    Threshold createThreshold(String accountId, ThresholdNeedle threshold);
}
