package com.diploma.panchev.nrfcloud.configuration;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.httpclient5.LogbookHttpExecHandler;

@Configuration
public class HttpClientConfiguration {
    @Bean
    @Primary
    public HttpClientBuilder getHttpClientBuilder(Logbook logbook) {
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(4))
                .setSocketTimeout(Timeout.ofSeconds(8))
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(4))
                .build();

        PoolingHttpClientConnectionManager connectionPoolManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setMaxConnPerRoute(100)
                        .setMaxConnTotal(1000)
                        .setDefaultConnectionConfig(connectionConfig)
                        .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionPoolManager)
                .addExecInterceptorLast("Logbook", new LogbookHttpExecHandler(logbook));
    }
}
