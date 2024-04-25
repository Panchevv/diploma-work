package com.diploma.panchev.nrfcloud.adapter.rest.impl;

import com.diploma.panchev.nrfcloud.adapter.rest.RestAdapter;
import com.diploma.panchev.nrfcloud.adapter.rest.configuration.NrfCloudProperties;
import com.diploma.panchev.nrfcloud.adapter.rest.exception.ApiException;
import com.diploma.panchev.nrfcloud.adapter.rest.model.AccountDevice;
import com.diploma.panchev.nrfcloud.adapter.rest.model.AccountInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRestAdapter implements RestAdapter {

    protected AccountInfo fetchAccountInfoFromApi(String bearerToken) {
        CloseableHttpClient httpClient = this.getHttpClient(bearerToken);
        ClassicRequestBuilder builder = ClassicRequestBuilder.get(this.getNrfCloudProperties().getBasePath() + "/account");

        try {
            return httpClient.execute(builder.build(), HttpClientContext.create(),
                    (response) -> processResponse(response, AccountInfo.class)
            );
        } catch (IOException e) {
            throw new ApiException("Exception during sending",e);
        }
    }

    protected AccountDevice deleteAccountDeviceFromApi(String bearerToken) {
        CloseableHttpClient httpClient = this.getHttpClient(bearerToken);
        ClassicRequestBuilder builder = ClassicRequestBuilder.delete(this.getNrfCloudProperties().getBasePath() + "/devices/account");

        try {
            return httpClient.execute(builder.build(), HttpClientContext.create(),
                    (response) -> processResponse(response, AccountDevice.class)
            );
        } catch (IOException e) {
            throw new ApiException("Exception during sending",e);
        }
    }

    protected AccountDevice createAccountDeviceFromApi(String bearerToken) {
        CloseableHttpClient httpClient = this.getHttpClient(bearerToken);
        ClassicRequestBuilder builder = ClassicRequestBuilder.post(this.getNrfCloudProperties().getBasePath() + "/devices/account");

        try {
            return httpClient.execute(builder.build(), HttpClientContext.create(),
                    (response) -> processResponse(response, AccountDevice.class)
            );
        } catch (IOException e) {
            throw new ApiException("Exception during sending",e);
        }
    }

    protected <T> T processResponse(ClassicHttpResponse response, Class<T> returnType) {
        int statusCode = response.getCode();
        if (statusCode == HttpStatus.SC_NO_CONTENT) {
            return null;
        }

        Map<String, List<String>> responseHeaders = transformResponseHeaders(response.getHeaders());
        String message = null;
        try {
            if (statusCode/100 == 2) {
                return this.getObjectMapper().readValue(response.getEntity().getContent(), returnType);
            } else {
                message = EntityUtils.toString(response.getEntity());
                throw new ApiException("Responded with not OK response", null, statusCode, responseHeaders, message);
            }
        } catch (IOException | ParseException e) {
            throw new ApiException("Exception during response processing", e, statusCode, responseHeaders, message);
        }
    }

    protected Map<String, List<String>> transformResponseHeaders(Header[] headers) {
        Map<String, List<String>> headersMap = new HashMap<>();
        for (Header header : headers) {
            List<String> valuesList = headersMap.get(header.getName());
            if (valuesList != null) {
                valuesList.add(header.getValue());
            } else {
                valuesList = new ArrayList<>();
                valuesList.add(header.getValue());
                headersMap.put(header.getName(), valuesList);
            }
        }
        return headersMap;
    }

    protected CloseableHttpClient getHttpClient(String bearerToken) {
        return this.getHttpClientBuilder()
                .addExecInterceptorBefore("Logbook", "MetaData", (httpRequest, scope, chain) -> {
                    httpRequest.setHeader("Authorization", "Bearer " + bearerToken);
                    httpRequest.setHeader("Content-Type", "application/json");
                    httpRequest.setHeader("Accept", "application/json");
                    return chain.proceed(httpRequest, scope);
                })
                .build();
    }

    abstract HttpClientBuilder getHttpClientBuilder();
    abstract NrfCloudProperties getNrfCloudProperties();
    abstract ObjectMapper getObjectMapper();
}
