package com.diploma.panchev.measurement.service.impl;

import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.DeviceMessage;
import com.diploma.panchev.measurement.service.MeasurementService;
import com.diploma.panchev.measurement.service.MessageService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MeasurementService measurementService;

    public MessageServiceImpl(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @Override
    public List<DeviceMeasurement> addDeviceMessage(@NonNull String deviceId, @NonNull DeviceMessage message) {
        if (CollectionUtils.isEmpty(message.getMeasurementList())) {
            log.warn("There is no available measurement for device ({}) in message ({})", deviceId, message.getMessageId());
            return List.of();
        }
        return message.getMeasurementList()
                .stream()
                .map(current -> measurementService.createMeasurement(UUID.fromString(message.getAccountId()), UUID.fromString(message.getGroupId()), deviceId, message.getMessageId(), current, message.getCreatedAt()))
                .toList();
    }
}
