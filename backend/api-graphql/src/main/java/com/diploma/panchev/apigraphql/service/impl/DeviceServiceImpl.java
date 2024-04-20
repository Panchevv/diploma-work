package com.diploma.panchev.apigraphql.service.impl;

import com.diploma.panchev.apigraphql.adapter.account.AccountAdapter;
import com.diploma.panchev.apigraphql.adapter.notification.NotificationAdapter;
import com.diploma.panchev.apigraphql.adapter.nrfcloud.NrfCloudAdapter;
import com.diploma.panchev.apigraphql.domain.*;
import com.diploma.panchev.apigraphql.domain.graphql.query.Connection;
import com.diploma.panchev.apigraphql.service.DeviceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Objects;
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

    @Override
    public Device createDevice(String accountId, String groupId, String deviceId, String name) {
        this.accountAdapter.createAccountDevice(accountId, deviceId, name);
        return this.assignDeviceGroup(accountId, groupId, deviceId);
    }

    @Override
    public Device updateDevice(String accountId, String deviceId, String name) {
        return this.accountAdapter.updateDevice(accountId, deviceId, name);
    }

    @Override
    public List<DeviceGroup> getDeviceGroups(String accountId) {
        return this.accountAdapter.getDeviceGroups(accountId);
    }

    @Override
    public Connection<Device> getAccountDevices(String accountId, Boolean ungrouped, String fromDeviceId, int pageSize) {
        Connection<Device> connection =
                this.accountAdapter.getAccountDevices(accountId, ungrouped, fromDeviceId, pageSize);
        Connection<Device> deviceConnection = new Connection<>();
        deviceConnection.setPageInfo(connection.getPageInfo());
        deviceConnection.setEdges(
                connection.getEdges()
        );
        return deviceConnection;
    }

    @Override
    public List<Device> getAccountDevices(String accountId, String deviceGroupId) {
        return this.accountAdapter.getAccountGroupDevices(accountId, deviceGroupId).stream()
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public Optional<Device> getAccountGroupDevice(String accountId, String deviceGroupId, String deviceId) {
        return this.accountAdapter.getAccountGroupDevice(accountId, deviceGroupId, deviceId)
                .stream()
                .filter(device -> Objects.equals(device.getDeviceId(), deviceId))
                .findAny();
    }

    @Override
    public List<Notification> getNotificationHistory(String groupId, Integer pageSize, String last, String deviceId) {
        return this.notificationAdapter.getNotificationHistory(groupId, pageSize, last, deviceId);
    }

    @Override
    @SneakyThrows
    public Flux<List<Measurement>> getGroupMeasurementUpdates(String token) {
        log.trace("getGroupMeasurementUpdates: json={}", token);
        return this.notificationAdapter.getMeasurementUpdates(token);
    }

    @Override
    @SneakyThrows
    public Flux<List<Notification>> getGroupNotificationUpdates(String token) {
        log.trace("getGroupNotificationUpdates: json={}", token);
        return this.notificationAdapter.getNotificationUpdates(token);
    }

    @Override
    public DeviceGroup deleteDeviceGroup(String accountId, String groupId) {
        DeviceGroup deviceGroup = this.accountAdapter.deleteDeviceGroup(accountId, groupId);
        this.nrfCloudAdapter.deleteSensorConfiguration(deviceGroup.getId(), null);
        return deviceGroup;
    }

    @Override
    public Device deleteDevice(String accountId, String deviceId) {
        Device device = this.accountAdapter.deleteDevice(accountId, deviceId);
        this.nrfCloudAdapter.deleteSensorConfiguration(null, deviceId);
        return device;
    }
}
