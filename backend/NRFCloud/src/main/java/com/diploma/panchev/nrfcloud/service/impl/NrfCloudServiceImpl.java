package com.diploma.panchev.nrfcloud.service.impl;

import com.diploma.panchev.nrfcloud.adapter.rest.RestAdapter;
import com.diploma.panchev.nrfcloud.adapter.rest.exception.ApiException;
import com.diploma.panchev.nrfcloud.domain.AccountDevice;
import com.diploma.panchev.nrfcloud.domain.AccountInfo;
import com.diploma.panchev.nrfcloud.domain.entity.ApiConfiguration;
import com.diploma.panchev.nrfcloud.domain.entity.MqttConfiguration;
import com.diploma.panchev.nrfcloud.repository.ApiConfigurationRepository;
import com.diploma.panchev.nrfcloud.repository.MqttConfigurationRepository;
import com.diploma.panchev.nrfcloud.service.NrfCloudService;
import com.diploma.panchev.nrfcloud.utils.Utils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;

@Slf4j
@Service
public class NrfCloudServiceImpl implements NrfCloudService {
    private final ApiConfigurationRepository apiConfigurationRepository;
    private final MqttConfigurationRepository mqttConfigurationRepository;
    private final RestAdapter restAdapter;

    public NrfCloudServiceImpl(ApiConfigurationRepository apiConfigurationRepository, MqttConfigurationRepository mqttConfigurationRepository, RestAdapter restAdapter) {
        this.apiConfigurationRepository = apiConfigurationRepository;
        this.mqttConfigurationRepository = mqttConfigurationRepository;
        this.restAdapter = restAdapter;
    }

    @Override
    public ApiConfiguration updateBearerToken(@NonNull String accountId, @NonNull String bearerToken) {
        ApiConfiguration entity = this.apiConfigurationRepository.findById(accountId)
                .orElseGet(() -> {
                    ApiConfiguration apiConfiguration = new ApiConfiguration();
                    apiConfiguration.setIsActive(true);
                    apiConfiguration.setId(accountId);
                    apiConfiguration.setCreatedAt(OffsetDateTime.now());
                    return apiConfiguration;
                });

        try {
            this.restAdapter.getAccountInfo(bearerToken);
            if (entity.getBearerToken() != null) {
                entity.setModifiedAt(OffsetDateTime.now());
            }
            entity.setBearerToken(bearerToken);
            return this.apiConfigurationRepository.save(entity);
        } catch (ApiException apiException) {
            throw new RuntimeException("Bearer token is invalid");
        }
    }

    @Override
    public MqttConfiguration generateAccountDevice(String accountId) {
        ApiConfiguration entity = this.apiConfigurationRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account is not set"));
        AccountInfo accountInfo = this.restAdapter.getAccountInfo(entity.getBearerToken());

        Optional<MqttConfiguration> byId = this.mqttConfigurationRepository.findById(accountId);
        MqttConfiguration configuration;
        if (byId.isPresent()) {
            try {
                this.restAdapter.deleteAccountDevice(entity.getBearerToken());
            } catch (Throwable th) {
                log.info("Account device is already deleted");
            }
            configuration = byId.get();
        } else {
            configuration = new MqttConfiguration();
            configuration.setId(accountId);

        }

        configuration.setHost(accountInfo.getMqttEndpoint());
        configuration.setPrefix(accountInfo.getMqttTopicPrefix());

        AccountDevice accountDevice = this.restAdapter.createAccountDevice(entity.getBearerToken());
        configuration.setClientId(accountDevice.getClientId());
        configuration.setCaCert(Utils.convertPemToByte(accountDevice.getCaCert(), "CERTIFICATE"));
        configuration.setClientCert(Utils.convertPemToByte(accountDevice.getClientCert(), "CERTIFICATE"));
        configuration.setPrivateKey(Utils.convertPemToByte(accountDevice.getPrivateKey(), "RSA PRIVATE KEY"));

        return this.mqttConfigurationRepository.save(configuration);
    }
}
