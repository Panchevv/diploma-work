package com.diploma.panchev.nrfcloud.utils;

import com.diploma.panchev.nrfcloud.adapter.mqtt.utils.MqttHelper;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MqttHelperTest {

    @Test
    public void testCorrectTopic() {
        Optional<String> s = MqttHelper.parseDeviceId("prod/c96a1765-377d-4d0f-bb36-14845be26865/m/d/nrfsim-93459657867849480000/d2c");
        assertTrue(s.isPresent());
        assertEquals(s.get(), "nrfsim-93459657867849480000");
    }
}
