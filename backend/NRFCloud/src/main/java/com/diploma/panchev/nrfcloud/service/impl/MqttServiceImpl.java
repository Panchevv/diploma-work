package com.diploma.panchev.nrfcloud.service.impl;

import com.diploma.panchev.nrfcloud.adapter.mqtt.MqttAdapter;
import com.diploma.panchev.nrfcloud.adapter.mqtt.configuration.MqttConfiguration;
import com.diploma.panchev.nrfcloud.adapter.mqtt.impl.MqttAdapterImpl;
import com.diploma.panchev.nrfcloud.domain.Measurement;
import com.diploma.panchev.nrfcloud.domain.MeasurementType;
import com.diploma.panchev.nrfcloud.repository.MqttConfigurationRepository;
import com.diploma.panchev.nrfcloud.repository.SensorConfigurationRepository;
import com.diploma.panchev.nrfcloud.service.MqttService;
import com.diploma.panchev.nrfcloud.service.NotificationService;
import com.diploma.panchev.nrfcloud.utils.Utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class MqttServiceImpl implements MqttService {
    private final Map<String, MqttAdapter> mqttAdapter = new ConcurrentHashMap<>();
    private final NotificationService notificationService;
    private final MqttConfigurationRepository mqttConfigurationRepository;
    private final SensorConfigurationRepository sensorConfigurationRepository;
    private final ObjectMapper objectMapper;

    public MqttServiceImpl(NotificationService notificationService, MqttConfigurationRepository mqttConfigurationRepository, SensorConfigurationRepository sensorConfigurationRepository, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.mqttConfigurationRepository = mqttConfigurationRepository;
        this.sensorConfigurationRepository = sensorConfigurationRepository;
        this.objectMapper = objectMapper;
    }

    public void consumeMessage(String deviceId, byte[] message) {
        this.sensorConfigurationRepository.findById(deviceId)
                .ifPresentOrElse(sensorConfiguration -> {
                    log.trace("consumerMessage: deviceId={} message=({} bytes)", deviceId, message.length);
                    try {
                        JsonNode jsonNode = this.objectMapper.readTree(message);
                        log.trace("consumerMessage: deviceId={} parsed json message = {}", deviceId, jsonNode);
                        if (jsonNode.has("appId") && jsonNode.has("messageType") && jsonNode.has("data")) {
                            String appId = jsonNode.get("appId").asText();
                            String messageType = jsonNode.get("messageType").asText();
                            OffsetDateTime when = jsonNode.has("ts") ? Utils.mapSecond(jsonNode.get("ts").asLong()) : OffsetDateTime.now();
                            if ("DATA".equals(messageType)) {
                                switch (appId) {
                                    case "HUMID" -> {
                                        double value = jsonNode.get("data").asDouble();
                                        this.notificationService.sendMeasurementUpdate(
                                                sensorConfiguration.getAccountId(),
                                                sensorConfiguration.getGroupId(),
                                                deviceId,
                                                new Measurement(MeasurementType.HUMIDITY, value),
                                                when
                                        );
                                    }
                                    case "TEMP" -> {
                                        double value = jsonNode.get("data").asDouble();
                                        this.notificationService.sendMeasurementUpdate(
                                                sensorConfiguration.getAccountId(),
                                                sensorConfiguration.getGroupId(),
                                                deviceId,
                                                new Measurement(MeasurementType.TEMPERATURE, value),
                                                when
                                        );
                                    }
                                    case "AIR_PRESS" -> {
                                        double value = jsonNode.get("data").asDouble();
                                        this.notificationService.sendMeasurementUpdate(
                                                sensorConfiguration.getAccountId(),
                                                sensorConfiguration.getGroupId(),
                                                deviceId,
                                                new Measurement(MeasurementType.AIR_PRESSURE, value),
                                                when
                                        );
                                    }
                                    case "VOLTAGE" -> {
                                        double value = jsonNode.get("data").asDouble();
                                        this.notificationService.sendMeasurementUpdate(
                                                sensorConfiguration.getAccountId(),
                                                sensorConfiguration.getGroupId(),
                                                deviceId,
                                                new Measurement(MeasurementType.BATTERY_VOLTAGE, value),
                                                when
                                        );
                                    }
                                    case "RSRP" -> {
                                        double value = jsonNode.get("data").asDouble();
                                        this.notificationService.sendMeasurementUpdate(
                                                sensorConfiguration.getAccountId(),
                                                sensorConfiguration.getGroupId(),
                                                deviceId,
                                                new Measurement(MeasurementType.RSRP, value),
                                                when
                                        );
                                    }
                                    default ->
                                            log.warn("Unrecognized appId. Received appId={} messageType={} deviceId={}",
                                                    appId, messageType, deviceId);
                                }
                            } else {
                                log.warn("Processing only messageType = DATA. Received appId={} messageType={} deviceId={}",
                                        appId, messageType, deviceId);
                            }
                        }
                    } catch (IOException e) {
                        log.error("There is a problem parsing JSON message={} for deviceId={}", message, deviceId);
                    }
                }, () -> log.warn("Sensor Configuration not found for deviceId={}. Skipping message consumption.", deviceId));
    }

    @Override
    public void restartMqttAdapter(String accountId) {
        this.mqttConfigurationRepository.findById(accountId)
                .ifPresent(mqttConfiguration -> {
                    if (this.mqttAdapter.containsKey(accountId)) {
                        try {
                            this.mqttAdapter.get(accountId).stop();
                        } catch (MqttException e) {
                            log.error("Error when stopping mqtt consumer for {}", accountId, e);
                        }
                    }
                    this.restartMqttAdapterInternal(mqttConfiguration);
                });
    }

    private void restartMqttAdapterInternal(com.diploma.panchev.nrfcloud.domain.entity.MqttConfiguration mqttConfiguration) {
        MqttConfiguration configuration = new MqttConfiguration(
                mqttConfiguration.getHost(), mqttConfiguration.getClientId(), mqttConfiguration.getPrefix(),
                mqttConfiguration.getClientCert(), mqttConfiguration.getPrivateKey()
        );
        MqttAdapter impl = new MqttAdapterImpl(configuration);
        impl.run(this::consumeMessage);
        this.mqttAdapter.put(mqttConfiguration.getId(), impl);
    }

    @PostConstruct
    public void init() {
        this.mqttConfigurationRepository.findAll()
                .forEach(this::restartMqttAdapterInternal);
    }

    @EventListener
    public void eventListener(ContextClosedEvent event) {
        log.info("Closing = {}", event);
        for (Map.Entry<String, MqttAdapter> adapter : this.mqttAdapter.entrySet()) {
            try {
                adapter.getValue().stop();
            } catch (MqttException exception) {
                log.error("Exception when stopping the mqtt adapter for accountId = {}", adapter.getKey(), exception);
            }
        }
    }
}
