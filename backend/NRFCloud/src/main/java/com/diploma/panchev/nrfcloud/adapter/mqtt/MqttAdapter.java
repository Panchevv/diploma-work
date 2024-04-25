package com.diploma.panchev.nrfcloud.adapter.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.function.BiConsumer;

public interface MqttAdapter {
    void run(BiConsumer<String, byte[]> consumer);

    void stop() throws MqttException;
}
