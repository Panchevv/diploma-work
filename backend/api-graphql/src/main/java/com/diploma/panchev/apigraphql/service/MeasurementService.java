package com.diploma.panchev.apigraphql.service;

import com.diploma.panchev.apigraphql.domain.Measurement;
import com.diploma.panchev.apigraphql.domain.MeasurementType;
import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;
import com.diploma.panchev.apigraphql.domain.graphql.query.Connection;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MeasurementService {
    Threshold createThreshold(String accountId, ThresholdNeedle threshold);

    Threshold editThreshold(String id, ThresholdNeedle threshold);

    Threshold deleteThreshold(String id);

    List<Threshold> getThresholds(String accountId);

    Optional<Measurement> getDeviceMeasurement(String deviceId, MeasurementType measurementType);

    Optional<Connection<Measurement>> getDeviceMeasurementHistory(String deviceId, MeasurementType type, OffsetDateTime from, OffsetDateTime to, String fromId, int pageSize);
}
