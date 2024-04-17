package com.diploma.panchev.apigraphql.adapter.measurement.configuration;

import com.diploma.panchev.apigraphql.utils.Utils;
import com.diploma.panchev.measurement.grpc.ThresholdServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class MeasurementConfiguration {
    public final static String MEASUREMENT_MANAGED_CHANNEL_BEAN =  "MEASUREMENT_MANAGED_CHANNEL_BEAN";

    @Bean(MEASUREMENT_MANAGED_CHANNEL_BEAN)
    public ManagedChannel createManagedChannel(MeasurementProperties properties) {
        log.trace("createManagedChannel: properties={}", properties);
        return Utils.createManagedChannel(properties);
    }

//    @Bean
//    public MeasurementServiceGrpc.MeasurementServiceBlockingStub getMeasurementServiceBlockStub(
//            @Qualifier(MEASUREMENT_MANAGED_CHANNEL_BEAN) ManagedChannel channel) {
//        return MeasurementServiceGrpc.newBlockingStub(channel);
//    }

    @Bean
    public ThresholdServiceGrpc.ThresholdServiceBlockingStub getThresholdServiceBlockStub(
            @Qualifier(MEASUREMENT_MANAGED_CHANNEL_BEAN) ManagedChannel channel) {
        return ThresholdServiceGrpc.newBlockingStub(channel);
    }
}
