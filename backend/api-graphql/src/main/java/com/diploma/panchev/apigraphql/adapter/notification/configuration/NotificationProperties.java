package com.diploma.panchev.apigraphql.adapter.notification.configuration;

import com.diploma.panchev.apigraphql.configuration.GrpcAdapterBaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("adapter.notification")
public class NotificationProperties extends GrpcAdapterBaseProperties {
}
