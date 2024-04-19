package com.diploma.panchev.apigraphql.adapter.notification.configuration;

import com.diploma.panchev.apigraphql.utils.Utils;
import com.diploma.panchev.notification.grpc.ReactorPushStreamServiceGrpc;
import com.diploma.panchev.notification.history.grpc.NotificationHistoryServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class NotificationConfiguration {
    public static final String NOTIFICATION_MANAGER_CHANNEL_BEAN =  "NOTIFICATION_MANAGER_CHANNEL_BEAN";

    @Bean(NOTIFICATION_MANAGER_CHANNEL_BEAN)
    public ManagedChannel createManagedChannel(NotificationProperties properties) {
        log.trace("createManagedChannel: properties={}", properties);
        return Utils.createManagedChannel(properties);
    }

    @Bean
    public ReactorPushStreamServiceGrpc.ReactorPushStreamServiceStub getNotificationServiceBlockingStub(
            @Qualifier(NOTIFICATION_MANAGER_CHANNEL_BEAN) ManagedChannel channel) {
        return ReactorPushStreamServiceGrpc.newReactorStub(channel).withDeadline(null);
    }

    @Bean
    public NotificationHistoryServiceGrpc.NotificationHistoryServiceBlockingStub getNotificationHistoryServiceStub (
            @Qualifier(NOTIFICATION_MANAGER_CHANNEL_BEAN) ManagedChannel channel) {
        return NotificationHistoryServiceGrpc.newBlockingStub(channel);
    }
}
