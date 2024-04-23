package com.diploma.panchev.notification.service.impl;

import com.diploma.panchev.messaging.MeasurementUpdateInternal;
import com.diploma.panchev.notification.domain.measurement.MeasurementUpdate;
import com.diploma.panchev.notification.service.MeasurementUpdateService;
import com.diploma.panchev.notification.service.mapper.NotificationServiceMapper;
import com.diploma.panchev.notification.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class MeasurementUpdateServiceImpl implements MeasurementUpdateService {
    private static final NotificationServiceMapper MAPPER = Mappers.getMapper(NotificationServiceMapper.class);
    private final Sinks.Many<MeasurementUpdate> sink = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    private final ReactiveKafkaConsumerTemplate<String, byte[]> kafkaConsumerTemplate;
    private final boolean enabled;
    public MeasurementUpdateServiceImpl(
            @Qualifier(Constants.KAFKA_UPDATE_MEASUREMENT_CONSUMER_BEAN) ReactiveKafkaConsumerTemplate<String, byte[]> kafkaConsumerTemplate,
            @Value(value = "${messaging.update-measurement-internal.enabled}") boolean enabled) {
        this.kafkaConsumerTemplate = kafkaConsumerTemplate;
        this.enabled = enabled;
    }

    public Flux<MeasurementUpdate> consume() {
        return this.kafkaConsumerTemplate
                .receiveAutoAck()
                .name("measurement")
                .doOnError(throwable -> log.error("Error happened while consuming : {}", throwable.getMessage()))
                .flatMap(record -> {
                    try {
                        return Flux.just(
                                MeasurementUpdateInternal.UpdateMeasurementInternal.parseFrom(record.value())
                        );
                    } catch (Throwable th) {
                        log.error("There is a problem parsing proto message");
                    }
                    return Flux.empty();
                })
                .filter(current -> !current.getDeviceId().isBlank() && current.hasGroupId())
                .map(MAPPER::map);
    }
    @PostConstruct
    public void start() {
        if (this.enabled) {
            this.consume().subscribe(sink::tryEmitNext);
        }
    }
    @Override
    public Flux<MeasurementUpdate> getMeasurementUpdate() {
        return this.sink.asFlux();
    }
}
