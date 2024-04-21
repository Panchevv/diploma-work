package com.diploma.panchev.account.service;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.AccountDevice;
import com.diploma.panchev.account.domain.AccountDeviceNeedle;
import com.diploma.panchev.account.domain.Relay;

import java.util.Optional;
import java.util.UUID;

public interface AccountDeviceService {
    Optional<AccountDevice> getAccountDevice(UUID accountId, String deviceId);

    AccountDevice addAccountDevice(Account account, String deviceId, String name);

    AccountDevice updateAccountDevice(UUID accountId, String deviceId, String name);

    Relay<AccountDevice> getDevices(AccountDeviceNeedle needle, String fromDeviceId, int size);

    AccountDevice removeAccountDevice(Account account, String deviceId);
}
