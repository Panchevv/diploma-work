package com.diploma.panchev.nrfcloud.adapter.rest.impl;

import com.diploma.panchev.nrfcloud.adapter.rest.configuration.NrfCloudProperties;
import com.diploma.panchev.nrfcloud.adapter.rest.exception.ApiException;
import com.diploma.panchev.nrfcloud.adapter.rest.mapper.RestMapper;
import com.diploma.panchev.nrfcloud.domain.AccountDevice;
import com.diploma.panchev.nrfcloud.domain.AccountInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class RestAdapterImpl extends AbstractRestAdapter{
    private final static RestMapper MAPPER = Mappers.getMapper(RestMapper.class);

    @Getter
    private final HttpClientBuilder httpClientBuilder;
    @Getter
    private final NrfCloudProperties nrfCloudProperties;
    @Getter
    private final ObjectMapper objectMapper;

    public RestAdapterImpl(HttpClientBuilder httpClientBuilder, NrfCloudProperties nrfCloudProperties, ObjectMapper objectMapper) {
        this.httpClientBuilder = httpClientBuilder;
        this.nrfCloudProperties = nrfCloudProperties;
        this.objectMapper = objectMapper;
    }

    @Override
    public AccountInfo getAccountInfo(String bearerToken) {
        try {
            return MAPPER.map(this.fetchAccountInfoFromApi(bearerToken));
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAccountDevice(String bearerToken) {
        this.deleteAccountDeviceFromApi(bearerToken);
    }

    @Override
    public AccountDevice createAccountDevice(String bearerToken) {
        try {
            return MAPPER.map(this.createAccountDeviceFromApi(bearerToken));
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }
}
