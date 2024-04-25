package com.diploma.panchev.nrfcloud.controller;

import com.diploma.panchev.nrf.grpc.NrfCloudGrpc;
import com.diploma.panchev.nrf.grpc.NrfCloudServiceGrpc;
import com.diploma.panchev.nrfcloud.controller.mapper.GrpcMapper;
import com.diploma.panchev.nrfcloud.domain.entity.MqttConfiguration;
import com.diploma.panchev.nrfcloud.domain.entity.SensorConfiguration;
import com.diploma.panchev.nrfcloud.repository.ApiConfigurationRepository;
import com.diploma.panchev.nrfcloud.repository.SensorConfigurationRepository;
import com.diploma.panchev.nrfcloud.service.MqttService;
import com.diploma.panchev.nrfcloud.service.NrfCloudService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.mapstruct.factory.Mappers;
import org.springframework.transaction.annotation.Transactional;

@GRpcService
public class NrfCloudServiceGrpcImpl extends NrfCloudServiceGrpc.NrfCloudServiceImplBase {
    private final static GrpcMapper MAPPER = Mappers.getMapper(GrpcMapper.class);

    private final ApiConfigurationRepository apiConfigurationRepository;
    private final SensorConfigurationRepository sensorConfigurationRepository;
    private final NrfCloudService nrfCloudService;
    private final MqttService mqttService;

    public NrfCloudServiceGrpcImpl(ApiConfigurationRepository apiConfigurationRepository, SensorConfigurationRepository sensorConfigurationRepository, NrfCloudService nrfCloudService, MqttService mqttService) {
        this.apiConfigurationRepository = apiConfigurationRepository;
        this.sensorConfigurationRepository = sensorConfigurationRepository;
        this.nrfCloudService = nrfCloudService;
        this.mqttService = mqttService;
    }

    @Override
    public void setAccountSettings(NrfCloudGrpc.SetAccountSettingsRequest request, StreamObserver<NrfCloudGrpc.SetAccountSettingsResponse> responseObserver) {
        responseObserver.onNext(
                NrfCloudGrpc.SetAccountSettingsResponse.newBuilder()
                        .setAccountSettings(
                                MAPPER.map(
                                        this.nrfCloudService.updateBearerToken(
                                                request.getAccountId(),
                                                request.getBearerToken()
                                        )
                                )
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void regenerateMqttSettings(NrfCloudGrpc.RegenerateMqttSettingsRequest request, StreamObserver<NrfCloudGrpc.RegenerateMqttSettingsResponse> responseObserver) {
        MqttConfiguration configuration = this.nrfCloudService.generateAccountDevice(request.getAccountId());
        this.mqttService.restartMqttAdapter(request.getAccountId());
        responseObserver.onNext(
                NrfCloudGrpc.RegenerateMqttSettingsResponse.newBuilder()
                        .setMqttSettings(
                                MAPPER.map(configuration)
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void setSensorConfiguration(NrfCloudGrpc.SetSensorConfigurationRequest request, StreamObserver<NrfCloudGrpc.SetSensorConfigurationResponse> responseObserver) {
        SensorConfiguration sensorConfiguration = this.sensorConfigurationRepository.findById(request.getDeviceId())
                .orElseGet(() -> {
                    SensorConfiguration newSensorConf = new SensorConfiguration();
                    newSensorConf.setDeviceId(request.getDeviceId());
                    newSensorConf.setAccountId(request.getAccountId());
                    return newSensorConf;
                });

        sensorConfiguration.setGroupId(request.getGroupId());
        sensorConfiguration = this.sensorConfigurationRepository.save(sensorConfiguration);

        responseObserver.onNext(
                NrfCloudGrpc.SetSensorConfigurationResponse.newBuilder()
                        .setAccountId(sensorConfiguration.getAccountId())
                        .setGroupId(sensorConfiguration.getGroupId())
                        .setDeviceId(sensorConfiguration.getDeviceId())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getAccountSetting(NrfCloudGrpc.GetAccountSettingRequest request, StreamObserver<NrfCloudGrpc.GetAccountSettingResponse> responseObserver) {
        responseObserver.onNext(
                NrfCloudGrpc.GetAccountSettingResponse.newBuilder()
                        .setAccountSettings(
                                this.apiConfigurationRepository.findById(request.getAccountId())
                                        .map(MAPPER::map)
                                        .orElseGet(() -> NrfCloudGrpc.AccountSettings.newBuilder().build())
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Transactional
    @Override
    public void deleteSensorConfiguration(NrfCloudGrpc.DeleteSensorConfigurationRequest request, StreamObserver<NrfCloudGrpc.DeleteSensorConfigurationResponse> responseObserver) {
        try {
            if (request.hasDeviceId())
                sensorConfigurationRepository.deleteByDeviceId(request.getDeviceId().getValue());
            if (request.hasGroupId())
                sensorConfigurationRepository.deleteAllByGroupId(request.getGroupId().getValue());

            responseObserver.onNext(
                    NrfCloudGrpc.DeleteSensorConfigurationResponse.newBuilder()
                            .setSuccess(true)
                            .build()
            );
        } catch (Exception e) {
            responseObserver.onNext(
                    NrfCloudGrpc.DeleteSensorConfigurationResponse.newBuilder()
                            .setSuccess(false)
                            .build()
            );
        } finally {
            responseObserver.onCompleted();
        }
    }
}
