package com.diploma.panchev.apigraphql.service;

import com.diploma.panchev.apigraphql.domain.NrfAccountSettings;

public interface NrfCloudService {
    NrfAccountSettings setNrfCloudAccount(String accountId, String bearerToken);
}
