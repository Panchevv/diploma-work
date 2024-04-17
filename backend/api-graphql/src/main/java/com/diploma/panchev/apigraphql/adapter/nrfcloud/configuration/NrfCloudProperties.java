package com.diploma.panchev.apigraphql.adapter.nrfcloud.configuration;

import com.diploma.panchev.apigraphql.configuration.GrpcAdapterBaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("adapter.nrfcloud")
public class NrfCloudProperties extends GrpcAdapterBaseProperties {
}
