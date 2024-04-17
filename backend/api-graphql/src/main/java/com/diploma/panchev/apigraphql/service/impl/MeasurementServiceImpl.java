package com.diploma.panchev.apigraphql.service.impl;

import com.diploma.panchev.apigraphql.adapter.measurement.MeasurementAdapter;
import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;
import com.diploma.panchev.apigraphql.service.MeasurementService;
import org.springframework.stereotype.Service;

@Service
public class MeasurementServiceImpl implements MeasurementService {
    private final MeasurementAdapter measurementAdapter;

    public MeasurementServiceImpl(MeasurementAdapter measurementAdapter) {
        this.measurementAdapter = measurementAdapter;
    }

    @Override
    public Threshold createThreshold(String accountId, ThresholdNeedle threshold) {
        return this.measurementAdapter.createThreshold(accountId, threshold);
    }
}
