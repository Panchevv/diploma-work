package com.diploma.panchev.account.service;

import com.diploma.panchev.account.domain.AccountDevice;

import java.util.Optional;
import java.util.UUID;

public interface AccountDeviceService {
    Optional<AccountDevice> getAccountDevice(UUID accountId, String deviceId);
}
