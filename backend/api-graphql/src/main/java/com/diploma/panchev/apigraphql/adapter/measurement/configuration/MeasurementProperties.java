package com.diploma.panchev.apigraphql.adapter.measurement.configuration;

import com.diploma.panchev.apigraphql.configuration.GrpcAdapterBaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("adapter.measurement")
public class MeasurementProperties extends GrpcAdapterBaseProperties {
}
