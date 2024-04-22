package com.diploma.panchev.measurement.service;

import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.DeviceMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {
    List<DeviceMeasurement> addDeviceMessage(String deviceId, DeviceMessage message);
}
