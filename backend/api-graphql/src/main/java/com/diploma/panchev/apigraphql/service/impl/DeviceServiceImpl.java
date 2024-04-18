package com.diploma.panchev.apigraphql.service.impl;

import com.diploma.panchev.apigraphql.adapter.account.AccountAdapter;
import com.diploma.panchev.apigraphql.domain.DeviceGroup;
import com.diploma.panchev.apigraphql.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {
    private final AccountAdapter accountAdapter;

    public DeviceServiceImpl(AccountAdapter accountAdapter) {
        this.accountAdapter = accountAdapter;
    }

    @Override
    public DeviceGroup createDeviceGroup(String accountId, String name) {
        return this.accountAdapter.createDeviceGroup(accountId, name);
    }

    @Override
    public Optional<DeviceGroup> getDeviceGroup(String accountId, String deviceGroupId) {
        return this.accountAdapter.getDeviceGroup(accountId, deviceGroupId);
    }

    @Override
    public DeviceGroup updateDeviceGroup(String accountId, String groupId, String name) {
        return this.accountAdapter.updateDeviceGroup(
                accountId, groupId, name
        );
    }
}
