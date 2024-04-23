package com.diploma.panchev.notification.utils;

import java.time.ZoneId;

public class Constants {
    public final static String KAFKA_UPDATE_MEASUREMENT_RECEIVER_BEAN = "KafkaUpdateMeasurementReceiverBean";
    public final static String KAFKA_UPDATE_MEASUREMENT_CONSUMER_BEAN = "KafkaUpdateMeasurementConsumerBean";
    public final static String KAFKA_EVENT_RECEIVER_BEAN = "KafkaEventReceiverBean";
    public final static String KAFKA_EVENT_CONSUMER_BEAN = "KafkaEventConsumerBean";
    public final static ZoneId CURRENT_ZONE = ZoneId.of("Europe/Zurich");
}
