package com.diploma.panchev.apigraphql.adapter.account;

import com.diploma.panchev.apigraphql.domain.Account;
import com.diploma.panchev.apigraphql.domain.Device;
import com.diploma.panchev.apigraphql.domain.DeviceGroup;

import java.util.List;
import java.util.Optional;

public interface AccountAdapter {
    Account createAccount(String name, String userId);

    List<Account> getAccounts(String userId);

    DeviceGroup createDeviceGroup(String accountId, String name);

    Optional<DeviceGroup> getDeviceGroup(String accountId, String deviceGroupId);

    DeviceGroup updateDeviceGroup(String accountId, String groupId, String name);

    Optional<Device> getAccountDevice(String accountId, String deviceId);

    Device assignDevice(String accountId, String groupId, String deviceId);
}
