package com.diploma.panchev.apigraphql.service.impl;

import com.diploma.panchev.apigraphql.adapter.nrfcloud.NrfCloudAdapter;
import com.diploma.panchev.apigraphql.domain.NrfAccountSettings;
import com.diploma.panchev.apigraphql.service.NrfCloudService;
import org.springframework.stereotype.Service;

@Service
public class NrfCloudServiceImpl implements NrfCloudService {
    private final NrfCloudAdapter nrfCloudAdapter;

    public NrfCloudServiceImpl(NrfCloudAdapter nrfCloudAdapter) {
        this.nrfCloudAdapter = nrfCloudAdapter;
    }

    @Override
    public NrfAccountSettings setNrfCloudAccount(String accountId, String bearerToken) {
        return this.nrfCloudAdapter.setAccountSettings(accountId, bearerToken)
                .flatMap(nrfAccountSettings ->
                        this.nrfCloudAdapter.regenerateMqttSettings(nrfAccountSettings.getAccountId())
                                .map(mqttSettings -> {
                                    nrfAccountSettings.setClientId(mqttSettings.getClientId());
                                    return nrfAccountSettings;
                                })
                )
                .orElse(null);
    }
}
