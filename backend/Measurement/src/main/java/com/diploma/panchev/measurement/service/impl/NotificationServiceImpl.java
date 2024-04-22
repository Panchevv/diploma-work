package com.diploma.panchev.measurement.service.impl;

import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.ThresholdHistory;
import com.diploma.panchev.measurement.service.NotificationService;
import com.diploma.panchev.measurement.service.mapper.NotificationMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Setter
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final static NotificationMapper MAPPER = Mappers.getMapper(NotificationMapper.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final String measurementInternalTopicName;
    private final String eventTopicName;

    public NotificationServiceImpl(KafkaTemplate<String, byte[]> kafkaTemplate,
                                   @Value("${messaging.update-measurement-internal.topic}") String measurementInternalTopicName,
                                   @Value("${messaging.event-notification.topic}") String eventTopicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.measurementInternalTopicName = measurementInternalTopicName;
        this.eventTopicName = eventTopicName;
    }

    @Override
    public void sendEvent(ThresholdHistory thresholdHistory) {
        log.info("sendEvent: thresholdHistory={}", thresholdHistory);
        UUID eventId = UUID.randomUUID();
        this.kafkaTemplate.send(
                this.eventTopicName,
                eventId.toString(),
                MAPPER.map(
                        eventId.toString(),
                        thresholdHistory.getGroupId(),
                        thresholdHistory.getDeviceId(),
                        thresholdHistory.getThreshold().getId().toString(),
                        thresholdHistory.getThreshold().getName(),
                        MAPPER.map(thresholdHistory),
                        thresholdHistory.getMeasurement().getWhen()
                ).toByteArray()
        );
    }

    @Override
    public void sendInternal(DeviceMeasurement measurement) {
        log.info("sendInternal: measurement={}", measurement);
        this.kafkaTemplate.send(
                this.measurementInternalTopicName,
                measurement.getMeasurementId(),
                MAPPER.mapInternal(measurement).toByteArray()
        );
    }
}
