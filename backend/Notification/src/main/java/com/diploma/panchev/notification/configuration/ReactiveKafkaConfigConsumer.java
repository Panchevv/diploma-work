package com.diploma.panchev.notification.configuration;

import com.diploma.panchev.notification.utils.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;

@Configuration
public class ReactiveKafkaConfigConsumer {
    @Bean(Constants.KAFKA_UPDATE_MEASUREMENT_RECEIVER_BEAN)
    public ReceiverOptions<String, byte[]> getKafkaMeasurementReceiverOptions(
            @Value(value = "${messaging.update-measurement-internal.topic}") String topic,
            KafkaProperties kafkaProperties) {
        ReceiverOptions<String, byte[]> basicReceiverOptions = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean(Constants.KAFKA_UPDATE_MEASUREMENT_CONSUMER_BEAN)
    public ReactiveKafkaConsumerTemplate<String, byte[]> getReactiveKafkaMeasurementConsumerTemplate(
            @Qualifier(Constants.KAFKA_UPDATE_MEASUREMENT_RECEIVER_BEAN) ReceiverOptions<String, byte[]> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }

    @Bean(Constants.KAFKA_EVENT_RECEIVER_BEAN)
    public ReceiverOptions<String, byte[]> getKafkaEventReceiverOptions(
            @Value(value = "${messaging.event-message.topic}") String topic,
            KafkaProperties kafkaProperties) {
        ReceiverOptions<String, byte[]> basicReceiverOptions = ReceiverOptions.create(kafkaProperties.buildConsumerProperties());
        return basicReceiverOptions.subscription(Collections.singletonList(topic));
    }

    @Bean(Constants.KAFKA_EVENT_CONSUMER_BEAN)
    public ReactiveKafkaConsumerTemplate<String, byte[]> getReactiveKafkaEventConsumerTemplate(
            @Qualifier(Constants.KAFKA_EVENT_RECEIVER_BEAN) ReceiverOptions<String, byte[]> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }
}
