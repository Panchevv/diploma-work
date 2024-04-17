package com.diploma.panchev.apigraphql.adapter.nrfcloud;

import com.diploma.panchev.apigraphql.domain.MqttSettings;
import com.diploma.panchev.apigraphql.domain.NrfAccountSettings;

import java.util.Optional;

public interface NrfCloudAdapter {
    Optional<NrfAccountSettings> setAccountSettings(String accountId, String bearerToken);

    Optional<MqttSettings> regenerateMqttSettings(String accountId);
}
