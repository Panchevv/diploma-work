package com.diploma.panchev.nrfcloud.adapter.mqtt.utils;

import java.util.Optional;

public final class MqttHelper {
    private MqttHelper() { }

    public static Optional<String> parseDeviceId(String topic) {
        if (topic == null || topic.isBlank()) {
            return Optional.empty();
        }
        String[] levels = topic.split("/");
        if (levels.length < 5) {
            return Optional.empty();
        }
        return Optional.of(levels[4]);
    }
}
