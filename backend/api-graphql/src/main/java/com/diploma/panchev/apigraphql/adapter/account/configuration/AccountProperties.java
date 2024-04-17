package com.diploma.panchev.apigraphql.adapter.account.configuration;

import com.diploma.panchev.apigraphql.configuration.GrpcAdapterBaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("adapter.account")
public class AccountProperties extends GrpcAdapterBaseProperties {
}
