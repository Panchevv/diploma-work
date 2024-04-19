package com.diploma.panchev.apigraphql.adapter.nrfcloud.impl;

import com.diploma.panchev.apigraphql.adapter.nrfcloud.NrfCloudAdapter;
import com.diploma.panchev.apigraphql.adapter.nrfcloud.mapper.NrfCloudMapper;
import com.diploma.panchev.apigraphql.domain.MqttSettings;
import com.diploma.panchev.apigraphql.domain.NrfAccountSettings;
import com.diploma.panchev.nrf.grpc.NrfCloudGrpc;
import com.diploma.panchev.nrf.grpc.NrfCloudServiceGrpc;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NrfCloudAdapterImpl implements NrfCloudAdapter {
    private final static NrfCloudMapper MAPPER = Mappers.getMapper(NrfCloudMapper.class);
    private final NrfCloudServiceGrpc.NrfCloudServiceBlockingStub grpcApi;

    public NrfCloudAdapterImpl(NrfCloudServiceGrpc.NrfCloudServiceBlockingStub grpcApi) {
        this.grpcApi = grpcApi;
    }

    @Override
    public Optional<NrfAccountSettings> setAccountSettings(String accountId, String bearerToken) {
        return Optional.ofNullable(
                        this.grpcApi.setAccountSettings(
                                NrfCloudGrpc.SetAccountSettingsRequest.newBuilder()
                                        .setAccountId(accountId)
                                        .setBearerToken(bearerToken)
                                        .build()
                        )
                )
                .filter(NrfCloudGrpc.SetAccountSettingsResponse::hasAccountSettings)
                .map(NrfCloudGrpc.SetAccountSettingsResponse::getAccountSettings)
                .map(MAPPER::map);
    }

    @Override
    public Optional<MqttSettings> regenerateMqttSettings(String accountId) {
        return Optional.ofNullable(
                        this.grpcApi.regenerateMqttSettings(
                                NrfCloudGrpc.RegenerateMqttSettingsRequest.newBuilder()
                                        .setAccountId(accountId)
                                        .build()
                        )
                )
                .filter(NrfCloudGrpc.RegenerateMqttSettingsResponse::hasMqttSettings)
                .map(NrfCloudGrpc.RegenerateMqttSettingsResponse::getMqttSettings)
                .map(MAPPER::map);
    }

    @Override
    public String setSensorConfiguration(String accountId, String groupId, String deviceId) {
        return this.grpcApi.setSensorConfiguration(
                NrfCloudGrpc.SetSensorConfigurationRequest.newBuilder()
                        .setAccountId(accountId)
                        .setGroupId(groupId)
                        .setDeviceId(deviceId)
                        .build()
        ).getDeviceId();
    }

    @Override
    public Optional<NrfAccountSettings> getAccountSettings(String accountId) {
        return Optional.ofNullable(
                        this.grpcApi.getAccountSetting(
                                NrfCloudGrpc.GetAccountSettingRequest.newBuilder()
                                        .setAccountId(accountId)
                                        .build()
                        )
                )
                .filter(NrfCloudGrpc.GetAccountSettingResponse::hasAccountSettings)
                .map(NrfCloudGrpc.GetAccountSettingResponse::getAccountSettings)
                .map(MAPPER::map);
    }
}
