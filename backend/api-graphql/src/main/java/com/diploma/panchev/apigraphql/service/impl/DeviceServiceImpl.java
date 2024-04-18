package com.diploma.panchev.apigraphql.service.impl;

import com.diploma.panchev.apigraphql.adapter.account.AccountAdapter;
import com.diploma.panchev.apigraphql.adapter.notification.NotificationAdapter;
import com.diploma.panchev.apigraphql.adapter.nrfcloud.NrfCloudAdapter;
import com.diploma.panchev.apigraphql.domain.Device;
import com.diploma.panchev.apigraphql.domain.DeviceGroup;
import com.diploma.panchev.apigraphql.domain.SubscriptionSession;
import com.diploma.panchev.apigraphql.service.DeviceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {
    private final AccountAdapter accountAdapter;
    private final NotificationAdapter notificationAdapter;
    private final NrfCloudAdapter nrfCloudAdapter;

    public DeviceServiceImpl(AccountAdapter accountAdapter, NotificationAdapter notificationAdapter, NrfCloudAdapter nrfCloudAdapter) {
        this.accountAdapter = accountAdapter;
        this.notificationAdapter = notificationAdapter;
        this.nrfCloudAdapter = nrfCloudAdapter;
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

    @Override
    @SneakyThrows
    public SubscriptionSession generateSubscriptionSession(String accountId, String groupId) {
        return this.notificationAdapter.createStream(accountId, groupId);
    }

    @Override
    public Optional<Device> getAccountDevice(String accountId, String deviceId) {
        return this.accountAdapter.getAccountDevice(accountId, deviceId);
    }

    @Override
    public Device assignDeviceGroup(String accountId, String groupId, String deviceId) {
        Device device = this.accountAdapter.assignDevice(accountId, groupId, deviceId);
        this.nrfCloudAdapter.setSensorConfiguration(accountId, groupId, deviceId);
        return device;
    }
}
