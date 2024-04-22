package com.diploma.panchev.measurement.messaging;

import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.ThresholdHistory;
import com.diploma.panchev.measurement.mapper.MessageReceiverMapper;
import com.diploma.panchev.measurement.service.MessageService;
import com.diploma.panchev.measurement.service.NotificationService;
import com.diploma.panchev.measurement.service.ThresholdService;
import com.diploma.panchev.messaging.MeasurementUpdate;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Setter
@Component
public class MessageReceiver {
    private static final MessageReceiverMapper MAPPER = Mappers.getMapper(MessageReceiverMapper.class);
    private final MessageService messageService;
    private final ThresholdService thresholdService;
    private final NotificationService notificationService;

    public MessageReceiver(MessageService messageService, ThresholdService thresholdService, NotificationService notificationService) {
        this.messageService = messageService;
        this.thresholdService = thresholdService;
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = {"${messaging.update-measurement.topic}"}, batch = "true", autoStartup = "${messaging.update-measurement.enabled}")
    private void consumeMeasurementUpdate(List<byte[]> messageList) {
        log.info("Receive messages = (count={})", messageList.size());
        List<DeviceMeasurement> deviceMeasurements = messageList.stream()
                .flatMap(bytes -> {
                    try {
                        return Stream.of(MeasurementUpdate.MeasurementUpdateMessage.parseFrom(bytes));
                    } catch (IOException ex) {
                        log.error("Error while parsing protobuf = {}", bytes, ex);
                    }
                    return Stream.empty();
                })
                .flatMap(message -> this.messageService.addDeviceMessage(message.getDeviceId(), MAPPER.map(message)).stream())
                .toList();
        log.info("Found {} measurements", deviceMeasurements.size());

        List<ThresholdHistory> thresholds = deviceMeasurements.stream()
                .flatMap(deviceMeasurement -> this.thresholdService.evaluateDeviceMeasurement(deviceMeasurement).stream())
                .toList();
        deviceMeasurements.forEach(this.notificationService::sendInternal);
        thresholds.forEach(this.notificationService::sendEvent);
    }
}
