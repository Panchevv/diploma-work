package com.diploma.panchev.apigraphql.service;

import com.diploma.panchev.apigraphql.domain.DeviceGroup;

import java.util.Optional;

public interface DeviceService {
    DeviceGroup createDeviceGroup(String accountId, String name);

    Optional<DeviceGroup> getDeviceGroup(String accountId, String id);

    DeviceGroup updateDeviceGroup(String accountId, String groupId, String name);
}
