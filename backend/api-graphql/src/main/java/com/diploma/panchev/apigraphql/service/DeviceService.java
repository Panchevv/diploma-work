package com.diploma.panchev.apigraphql.service;

import com.diploma.panchev.apigraphql.domain.Device;
import com.diploma.panchev.apigraphql.domain.DeviceGroup;
import com.diploma.panchev.apigraphql.domain.SubscriptionSession;
import com.diploma.panchev.apigraphql.domain.graphql.query.Connection;

import java.util.List;
import java.util.Optional;

public interface DeviceService {
    DeviceGroup createDeviceGroup(String accountId, String name);

    Optional<DeviceGroup> getDeviceGroup(String accountId, String id);

    DeviceGroup updateDeviceGroup(String accountId, String groupId, String name);

    SubscriptionSession generateSubscriptionSession(String accountId, String groupId);

    Optional<Device> getAccountDevice(String accountId, String deviceId);

    Device assignDeviceGroup(String accountId, String groupId, String deviceId);

    Device createDevice(String accountId, String groupId, String deviceId, String name);

    Device updateDevice(String accountId, String deviceId, String name);

    List<DeviceGroup> getDeviceGroups(String accountId);

    Connection<Device> getAccountDevices(String accountId, Boolean ungrouped, String fromDeviceId, int pageSize);
}
