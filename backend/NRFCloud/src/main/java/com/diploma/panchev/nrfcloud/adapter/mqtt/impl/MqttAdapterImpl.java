package com.diploma.panchev.nrfcloud.adapter.mqtt.impl;

import com.diploma.panchev.nrfcloud.adapter.mqtt.MqttAdapter;
import com.diploma.panchev.nrfcloud.adapter.mqtt.configuration.MqttConfiguration;
import com.diploma.panchev.nrfcloud.adapter.mqtt.utils.MqttHelper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.function.BiConsumer;

@Slf4j
public class MqttAdapterImpl implements MqttAdapter {
    private MqttClient mqttClient = null;
    private MemoryPersistence persistence = null;
    private MqttConfiguration configuration;

    public MqttAdapterImpl(MqttConfiguration configuration) {
        this.persistence = new MemoryPersistence();
        this.configuration = configuration;
    }

    @Override
    public void run(@NonNull BiConsumer<String, byte[]> consumer) {
        try {
            this.mqttClient = new MqttClient("ssl://" + this.configuration.getHost() +":8883", this.configuration.getClientId(), persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setSocketFactory(this.configuration.getSslContext().getSocketFactory());
            this.mqttClient.setCallback(
                    new MqttCallbackExtended() {
                        @Override
                        public void connectComplete(boolean reconnect, String serverURI) {
                            try {
                                mqttClient.subscribe(configuration.getPrefix() + "m/d/+/d2c");
                            } catch (MqttException e) {
                                log.error("MQTT subscribe error: {}", e.getMessage(), e);
                            }
                        }

                        @Override
                        public void connectionLost(Throwable cause) {
                            log.error("MQTT Error: ", cause);
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage message) {
                            log.trace("Message arrived on topic={}: id={} message={} qos={}",
                                    topic,
                                    message.getId(),
                                    new String(message.getPayload()),
                                    message.getQos()
                            );
                            MqttHelper.parseDeviceId(topic)
                                    .ifPresent(deviceId -> consumer.accept(deviceId, message.getPayload()));
                        }

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken token) {
                            log.info("Delivery completed: {}", token);
                        }
                    }
            );
            this.mqttClient.connect(options);
            log.error("MQTT connection established");
        } catch (MqttException e) {
            log.error("MQTT connection error: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Generic exception: {}", e.getMessage(), e);
        }
    }

    @Override
    public void stop() throws MqttException {
        if (this.mqttClient != null && this.mqttClient.isConnected()) {
            this.mqttClient.disconnect();
        }
    }
}
