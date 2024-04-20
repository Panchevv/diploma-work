package com.diploma.panchev.apigraphql.adapter.notification.impl;

import com.diploma.panchev.apigraphql.adapter.notification.NotificationAdapter;
import com.diploma.panchev.apigraphql.adapter.notification.mapper.NotificationMapper;
import com.diploma.panchev.apigraphql.domain.Measurement;
import com.diploma.panchev.apigraphql.domain.Notification;
import com.diploma.panchev.apigraphql.domain.SubscriptionSession;
import com.diploma.panchev.notification.grpc.NotificationGrpc;
import com.diploma.panchev.notification.grpc.ReactorPushStreamServiceGrpc;
import com.diploma.panchev.notification.history.grpc.NotificationHistoryGrpc;
import com.diploma.panchev.notification.history.grpc.NotificationHistoryServiceGrpc;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class NotificationAdapterImpl implements NotificationAdapter {
    private static final NotificationMapper MAPPER = Mappers.getMapper(NotificationMapper.class);
    private final ReactorPushStreamServiceGrpc.ReactorPushStreamServiceStub grpc;
    private final NotificationHistoryServiceGrpc.NotificationHistoryServiceBlockingStub notificationHistoryServiceBlockingStub;

    public NotificationAdapterImpl(ReactorPushStreamServiceGrpc.ReactorPushStreamServiceStub grpc, NotificationHistoryServiceGrpc.NotificationHistoryServiceBlockingStub notificationHistoryServiceBlockingStub) {
        this.grpc = grpc;
        this.notificationHistoryServiceBlockingStub = notificationHistoryServiceBlockingStub;
    }

    @Override
    public SubscriptionSession createStream(String accountId, String groupId) {
        return this.grpc.createStream(
                        NotificationGrpc.CreateStreamRequest.newBuilder()
                                .setAccountId(accountId)
                                .setGroupId(groupId)
                                .build()
                )
                .doOnError(th -> log.error("createStream: {}", th.getMessage(), th))
                .map(MAPPER::map)
                .block(Duration.of(1, ChronoUnit.MINUTES));
    }

    @Override
    public List<Notification> getNotificationHistory(String groupId, Integer pageSize, String last, String deviceId) {
        NotificationHistoryGrpc.GetNotificationHistoryRequest.Builder request = NotificationHistoryGrpc.GetNotificationHistoryRequest
                .newBuilder()
                .setGroupId(groupId);
        if (pageSize != null) {
            request.setPageSize(Int32Value.newBuilder().setValue(pageSize).build());
        }
        if (last != null) {
            request.setLast(StringValue.newBuilder().setValue(last).build());
        }
        if (deviceId != null) {
            request.setDeviceId(StringValue.newBuilder().setValue(deviceId).build());
        }
        return this.notificationHistoryServiceBlockingStub.getNotificationHistory(request.build())
                .getEventsList()
                .stream()
                .map(MAPPER::map)
                .toList();
    }

    @Override
    public Flux<List<Measurement>> getMeasurementUpdates(String streamId) {
        return this.grpc.getMeasurementStreamUpdate(
                        NotificationGrpc.GetMeasurementStreamUpdateRequest.newBuilder()
                                .setStreamId(streamId)
                                .build()
                )
                .doOnError(th -> log.error("getMeasurementUpdates: {}", th.getMessage(), th))
                .doOnComplete(() -> log.trace("getMeasurementUpdates: flux completes"))
                .doOnCancel(() -> log.trace("getMeasurementUpdates: flux cancelled"))
                .map(MAPPER::map)
                .buffer(Duration.ofSeconds(10))
                .filter(list -> !list.isEmpty())
                .map(list -> {
                    Map<String, Measurement> measurementMap = new HashMap<>();
                    for (Measurement current: list) {
                        measurementMap.put(current.getDeviceId() + "#" + current.getType(), current);
                    }
                    return new LinkedList<>(measurementMap.values());
                });
    }

    @Override
    public Flux<List<Notification>> getNotificationUpdates(String streamId) {
        return this.grpc.getNotificationsStreamUpdate(
                        NotificationGrpc.GetNotificationsStreamUpdateRequest.newBuilder()
                                .setStreamId(streamId)
                                .build()
                )
                .doOnError(th -> log.error("getMeasurementUpdates: {}", th.getMessage(), th))
                .doOnComplete(() -> log.trace("getMeasurementUpdates: flux completes"))
                .doOnCancel(() -> log.trace("getMeasurementUpdates: flux cancelled"))
                .map(MAPPER::map)
                .buffer(Duration.ofSeconds(10));
    }
}
