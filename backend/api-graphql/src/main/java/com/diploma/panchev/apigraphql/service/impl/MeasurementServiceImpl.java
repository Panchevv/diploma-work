package com.diploma.panchev.apigraphql.service.impl;

import com.diploma.panchev.apigraphql.adapter.measurement.MeasurementAdapter;
import com.diploma.panchev.apigraphql.domain.Measurement;
import com.diploma.panchev.apigraphql.domain.MeasurementType;
import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;
import com.diploma.panchev.apigraphql.domain.graphql.query.Connection;
import com.diploma.panchev.apigraphql.service.MeasurementService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<Measurement> getDeviceMeasurement(String deviceId, MeasurementType measurementType) {
        return measurementAdapter.getDeviceMeasurement(deviceId, measurementType);
    }

    @Override
    public Optional<Connection<Measurement>> getDeviceMeasurementHistory(String deviceId, MeasurementType type, OffsetDateTime from, OffsetDateTime to, String fromId, int pageSize) {
        return measurementAdapter.getDeviceMeasurementHistory(deviceId, type, from, to, fromId, pageSize);
    }
}
