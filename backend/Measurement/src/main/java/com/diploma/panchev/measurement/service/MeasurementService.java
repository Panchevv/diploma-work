package com.diploma.panchev.measurement.service;

import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.Measurement;
import com.diploma.panchev.measurement.domain.MeasurementType;
import com.diploma.panchev.measurement.domain.Relay;
import com.diploma.panchev.measurement.domain.entity.DeviceMeasurementEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface MeasurementService {
    List<DeviceMeasurementEntity> getLastMeasurements(String deviceId);

    Relay<DeviceMeasurementEntity> getMeasurementHistory(String deviceId, MeasurementType type, OffsetDateTime from, OffsetDateTime to, Long fromId, Integer pageSize);

    DeviceMeasurement createMeasurement(UUID accountId, UUID groupId, String deviceId, String messageId, Measurement measurement, OffsetDateTime when);
}
