package com.diploma.panchev.nrfcloud.service.impl;

import com.diploma.panchev.messaging.MeasurementUpdate;
import com.diploma.panchev.nrfcloud.domain.Measurement;
import com.diploma.panchev.nrfcloud.service.NotificationService;
import com.diploma.panchev.nrfcloud.service.mapper.NotificationMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {
    private final static NotificationMapper MAPPER = Mappers.getMapper(NotificationMapper.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final String measurementTopic;

    public NotificationServiceImpl(KafkaTemplate<String, byte[]> kafkaTemplate,
                                   @Value("${messaging.update-measurement.topic}") String measurementTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.measurementTopic = measurementTopic;
    }

    @Override
    public void sendMeasurementUpdate(String accountId, String groupId, String deviceId, Measurement measurement, OffsetDateTime when) {
        log.trace("sendMeasurementUpdate: deviceId={} measurement={} when={}", deviceId, measurement, when);
        UUID messageId = UUID.randomUUID();
        MeasurementUpdate.MeasurementUpdateMessage message = MAPPER.map(messageId.toString(), accountId, deviceId, groupId, List.of(measurement), when);
        this.kafkaTemplate.send(this.measurementTopic, messageId.toString(), message.toByteArray());
    }
}
