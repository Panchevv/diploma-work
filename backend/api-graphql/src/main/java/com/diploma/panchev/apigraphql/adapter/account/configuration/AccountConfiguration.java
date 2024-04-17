package com.diploma.panchev.apigraphql.adapter.account.configuration;

import com.diploma.panchev.account.grpc.AccountServiceGrpc;
import com.diploma.panchev.apigraphql.utils.Utils;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AccountConfiguration {
    public final static String ACCOUNT_MANAGED_CHANNEL_BEAN =  "ACCOUNT_MANAGED_CHANNEL_BEAN";

    @Bean(ACCOUNT_MANAGED_CHANNEL_BEAN)
    public ManagedChannel createManagedChannel(AccountProperties properties) {
        log.trace("createManagedChannel: properties={}", properties);
        return Utils.createManagedChannel(properties);
    }

    @Bean
    public AccountServiceGrpc.AccountServiceBlockingStub getAccountServiceBlockStub(
            @Qualifier(ACCOUNT_MANAGED_CHANNEL_BEAN) ManagedChannel channel
    ) {
        return AccountServiceGrpc.newBlockingStub(channel);
    }
}
