package com.diploma.panchev.notification.service.impl;

import com.diploma.panchev.messaging.events.EventsProto;
import com.diploma.panchev.notification.domain.event.Event;
import com.diploma.panchev.notification.domain.event.entity.EventEntity;
import com.diploma.panchev.notification.domain.event.entity.ThresholdEntity;
import com.diploma.panchev.notification.repository.EventRepository;
import com.diploma.panchev.notification.repository.MeasurementThresholdRepository;
import com.diploma.panchev.notification.service.EventUpdateService;
import com.diploma.panchev.notification.service.mapper.NotificationServiceMapper;
import com.diploma.panchev.notification.utils.Constants;
import com.google.protobuf.InvalidProtocolBufferException;
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
public class EventUpdateServiceImpl implements EventUpdateService {
    private static final NotificationServiceMapper MAPPER = Mappers.getMapper(NotificationServiceMapper.class);
    private final Sinks.Many<Event> sink = Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    private final ReactiveKafkaConsumerTemplate<String, byte[]> kafkaConsumerTemplate;
    private final boolean enabled;

    private final EventRepository eventRepository;
    private final MeasurementThresholdRepository thresholdRepository;

    public EventUpdateServiceImpl(
            @Qualifier(Constants.KAFKA_EVENT_CONSUMER_BEAN) ReactiveKafkaConsumerTemplate<String, byte[]> kafkaConsumerTemplate,
            @Value(value = "${messaging.event-message.enabled}") boolean enabled, EventRepository eventRepository,
            MeasurementThresholdRepository thresholdRepository
    ) {
        this.kafkaConsumerTemplate = kafkaConsumerTemplate;
        this.enabled = enabled;
        this.eventRepository = eventRepository;
        this.thresholdRepository = thresholdRepository;
    }

    public Flux<Event> consume() {
        return this.kafkaConsumerTemplate
                .receiveAutoAck()
                .name("event")
                .doOnError(throwable -> log.error("Error happened while consuming : {}", throwable.getMessage()))
                .flatMap(kafkaRecord -> {
                    try {
                        return Flux.just(
                                EventsProto.EventMessage.parseFrom(kafkaRecord.value())
                        );
                    } catch (InvalidProtocolBufferException ex) {
                        log.error("There is a problem parsing proto message", ex);
                    }
                    return Flux.empty();
                })
                .filter(eventMessage -> !eventMessage.getDeviceId().isBlank())
                .map(MAPPER::map)
                .flatMap(event -> {
                    if (event.getThreshold() != null) {
                        event.getThreshold().setId(event.getReferenceId());
                        event.getThreshold().setThresholdName(event.getName());
                        return Flux.just(event);
                    }
                    return Flux.empty();
                })
                .doOnNext(this::persistEvent);
    }

    private void persistEvent(Event event) {
        EventEntity eventEntity = this.eventRepository.save(MAPPER.mapToEntity(event));

        if (event.getThreshold() != null) {
            ThresholdEntity thresholdEntity = MAPPER.map(event.getThreshold());
            thresholdEntity.setEvent(eventEntity);
            this.thresholdRepository.saveAndFlush(thresholdEntity);
        }
    }

    @PostConstruct
    public void start() {
        if (this.enabled) {
            this.consume().subscribe(sink::tryEmitNext);
        }
    }

    @Override
    public Flux<Event> getEventUpdate() {
        return this.sink.asFlux();
    }
}
