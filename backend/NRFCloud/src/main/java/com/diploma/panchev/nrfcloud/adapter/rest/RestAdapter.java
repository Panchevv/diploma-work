package com.diploma.panchev.nrfcloud.adapter.rest;

import com.diploma.panchev.nrfcloud.domain.AccountDevice;
import com.diploma.panchev.nrfcloud.domain.AccountInfo;

public interface RestAdapter {
    AccountInfo getAccountInfo(String bearerToken);

    void deleteAccountDevice(String bearerToken);

    AccountDevice createAccountDevice(String bearerToken);
}
