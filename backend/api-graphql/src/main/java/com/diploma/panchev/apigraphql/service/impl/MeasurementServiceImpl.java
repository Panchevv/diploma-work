package com.diploma.panchev.apigraphql.service.impl;

import com.diploma.panchev.apigraphql.adapter.measurement.MeasurementAdapter;
import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;
import com.diploma.panchev.apigraphql.service.MeasurementService;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Threshold editThreshold(String id, ThresholdNeedle threshold) {
        return this.measurementAdapter.editThreshold(id, threshold);
    }

    @Override
    public Threshold deleteThreshold(String id) {
        return this.measurementAdapter.deleteThreshold(id);
    }

    @Override
    public List<Threshold> getThresholds(String accountId) {
        return this.measurementAdapter.getThresholds(accountId);
    }
}
