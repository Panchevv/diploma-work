package com.diploma.panchev.nrfcloud.service;

import com.diploma.panchev.nrfcloud.domain.entity.ApiConfiguration;
import com.diploma.panchev.nrfcloud.domain.entity.MqttConfiguration;

public interface NrfCloudService {
    ApiConfiguration updateBearerToken(String accountId, String bearerToken);

    MqttConfiguration generateAccountDevice(String accountId);
}
