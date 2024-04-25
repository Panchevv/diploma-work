package com.diploma.panchev.nrfcloud.service.impl;

import com.diploma.panchev.nrfcloud.adapter.mqtt.MqttAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class MqttServiceImplTest {
    private final static String MOCK_DEVICE_ID = "nrfsim-802299431599579100000";

    private MqttAdapter mqttAdapter;
    private MqttServiceImpl mqttService;

    @BeforeEach
    public void beforeEach() {
        this.mqttAdapter = Mockito.mock(MqttAdapter.class);
//        this.mqttService = new MqttServiceImpl(null, null, null);
    }

    @Test
    void consumerMessage() {
//        this.mqttService.consumeMessage(MOCK_DEVICE_ID );
    }
}
