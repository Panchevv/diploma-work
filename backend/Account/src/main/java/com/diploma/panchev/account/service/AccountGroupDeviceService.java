package com.diploma.panchev.account.service;

import com.diploma.panchev.account.domain.AccountGroupDevice;
import lombok.NonNull;

import java.util.UUID;

public interface AccountGroupDeviceService {
    AccountGroupDevice addAccountGroupDevice(@NonNull UUID accountId, @NonNull UUID groupId, @NonNull String deviceId);
}
