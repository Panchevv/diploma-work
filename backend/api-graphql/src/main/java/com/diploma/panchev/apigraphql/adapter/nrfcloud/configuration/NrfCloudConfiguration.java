package com.diploma.panchev.apigraphql.adapter.nrfcloud.configuration;

import com.diploma.panchev.apigraphql.utils.Utils;
import com.diploma.panchev.nrf.grpc.NrfCloudServiceGrpc;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class NrfCloudConfiguration {
    public final static String NRF_CLOUD_MANAGED_CHANNEL_BEAN = "NRF_CLOUD_MANAGED_CHANNEL_BEAN";

    @Bean(NRF_CLOUD_MANAGED_CHANNEL_BEAN)
    public ManagedChannel createManagedChannel(NrfCloudProperties properties) {
        log.trace("createManagedChannel: properties={}", properties);
        return Utils.createManagedChannel(properties);
    }

    @Bean
    public NrfCloudServiceGrpc.NrfCloudServiceBlockingStub getNrfCloudServiceBlockingStub(
            @Qualifier(NRF_CLOUD_MANAGED_CHANNEL_BEAN) ManagedChannel channel
    ) {
        return NrfCloudServiceGrpc.newBlockingStub(channel);
    }
}
