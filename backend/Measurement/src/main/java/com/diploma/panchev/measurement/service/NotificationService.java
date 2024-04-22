package com.diploma.panchev.measurement.service;

import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.ThresholdHistory;

public interface NotificationService {
    void sendEvent(ThresholdHistory thresholdHistory);

    void sendInternal(DeviceMeasurement thresholdHistory);
}
