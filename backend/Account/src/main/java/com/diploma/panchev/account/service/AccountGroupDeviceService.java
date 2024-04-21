package com.diploma.panchev.account.service;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.AccountGroup;
import com.diploma.panchev.account.domain.AccountGroupDevice;
import com.diploma.panchev.account.domain.AccountGroupDeviceNeedle;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface AccountGroupDeviceService {
    AccountGroupDevice addAccountGroupDevice(@NonNull UUID accountId, @NonNull UUID groupId, @NonNull String deviceId);

    List<AccountGroupDevice> getAccountGroupDevices(AccountGroupDeviceNeedle needle);

    AccountGroupDevice removeAccountGroupDevice(Account account, AccountGroup accountGroup, AccountGroupDevice device);

    Collection<AccountGroupDevice> getAccountGroupDevices(Account account, AccountGroup accountGroup);
}
