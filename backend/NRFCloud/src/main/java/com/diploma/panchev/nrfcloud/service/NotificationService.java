package com.diploma.panchev.nrfcloud.service;

import com.diploma.panchev.nrfcloud.domain.Measurement;

import java.time.OffsetDateTime;

public interface NotificationService {
    void sendMeasurementUpdate(String accountId, String groupId, String deviceId, Measurement measurement, OffsetDateTime when);
}
