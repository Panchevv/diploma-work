package com.diploma.panchev.notification.controller;

import com.diploma.panchev.notification.controller.mapper.ControllerMapper;
import com.diploma.panchev.notification.grpc.NotificationGrpc;
import com.diploma.panchev.notification.grpc.ReactorPushStreamServiceGrpc;
import com.diploma.panchev.notification.service.EventUpdateService;
import com.diploma.panchev.notification.service.MeasurementUpdateService;
import com.diploma.panchev.notification.service.PushStreamService;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.mapstruct.factory.Mappers;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GRpcService
@Slf4j
public class PushStreamGrpcServiceImpl extends ReactorPushStreamServiceGrpc.PushStreamServiceImplBase {
    private static final ControllerMapper MAPPER = Mappers.getMapper(ControllerMapper.class);

    private final PushStreamService pushStreamService;
    private final MeasurementUpdateService measurementUpdateService;
    private final EventUpdateService eventUpdateService;

    public PushStreamGrpcServiceImpl(PushStreamService pushStreamService, MeasurementUpdateService measurementUpdateService, EventUpdateService eventUpdateService) {
        this.pushStreamService = pushStreamService;
        this.measurementUpdateService = measurementUpdateService;
        this.eventUpdateService = eventUpdateService;
    }

    @Override
    public Mono<NotificationGrpc.CreateStreamResponse> createStream(Mono<NotificationGrpc.CreateStreamRequest> request) {
        return request
                .map(rq -> this.pushStreamService.createPushStream(rq.getAccountId(), rq.getGroupId()))
                .map(res ->
                        NotificationGrpc.CreateStreamResponse.newBuilder()
                                .setStreamId(res.streamId())
                                .setExpiresAt(MAPPER.map(res.expiresAt()))
                                .build()
                );
    }

    @Override
    public Flux<NotificationGrpc.UpdatedMeasurementMessage> getMeasurementStreamUpdate(Mono<NotificationGrpc.GetMeasurementStreamUpdateRequest> request) {
        return request
                .flatMapMany(rq ->
                        this.pushStreamService.getPushStream(rq.getStreamId())
                                .map(Flux::just)
                                .orElseGet(Flux::empty)
                )
                .flatMap(req ->
                        this.measurementUpdateService.getMeasurementUpdate()
                                .filter(current -> current.getGroupId().equals(req.groupId()))
                                .map(MAPPER::map)
                );
    }

    @Override
    public Flux<NotificationGrpc.UpdatedEventMessage> getNotificationsStreamUpdate(Mono<NotificationGrpc.GetNotificationsStreamUpdateRequest> request) {
        return request
                .flatMapMany(rq ->
                        this.pushStreamService.getPushStream(rq.getStreamId())
                                .map(Flux::just)
                                .orElseGet(Flux::empty)
                )
                .flatMap(req ->
                        this.eventUpdateService.getEventUpdate()
                                .filter(current -> current.getGroupId().equals(req.groupId()))
                                .map(MAPPER::map)
                );
    }
}
