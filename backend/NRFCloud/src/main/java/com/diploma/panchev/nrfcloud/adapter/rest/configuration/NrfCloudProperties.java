package com.diploma.panchev.nrfcloud.adapter.rest.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("nrf-cloud.rest")
public class NrfCloudProperties {
    private String basePath;
}
