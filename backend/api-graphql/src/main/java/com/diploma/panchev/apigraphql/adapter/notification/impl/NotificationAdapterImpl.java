package com.diploma.panchev.apigraphql.adapter.notification.impl;

import com.diploma.panchev.apigraphql.adapter.notification.NotificationAdapter;
import com.diploma.panchev.apigraphql.adapter.notification.mapper.NotificationMapper;
import com.diploma.panchev.apigraphql.domain.SubscriptionSession;
import com.diploma.panchev.notification.grpc.NotificationGrpc;
import com.diploma.panchev.notification.grpc.ReactorPushStreamServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
public class NotificationAdapterImpl implements NotificationAdapter {
    private static final NotificationMapper MAPPER = Mappers.getMapper(NotificationMapper.class);
    private final ReactorPushStreamServiceGrpc.ReactorPushStreamServiceStub grpc;
//    private final NotificationHistoryServiceGrpc.NotificationHistoryServiceBlockingStub notificationHistoryServiceBlockingStub;

    public NotificationAdapterImpl(ReactorPushStreamServiceGrpc.ReactorPushStreamServiceStub grpc) {
        this.grpc = grpc;
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
}
