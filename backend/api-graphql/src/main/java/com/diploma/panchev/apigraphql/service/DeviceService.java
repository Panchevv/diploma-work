package com.diploma.panchev.apigraphql.service;

import com.diploma.panchev.apigraphql.domain.*;
import com.diploma.panchev.apigraphql.domain.graphql.query.Connection;
import reactor.core.publisher.Flux;

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

    List<Device> getAccountDevices(String accountId, String deviceGroupId);

    Optional<Device> getAccountGroupDevice(String accountId, String deviceGroupId, String deviceId);

    List<Notification> getNotificationHistory(String groupId, Integer pageSize, String last, String deviceId);

    Flux<List<Measurement>> getGroupMeasurementUpdates(String token);

    Flux<List<Notification>> getGroupNotificationUpdates(String token);

    DeviceGroup deleteDeviceGroup(String accountId, String groupId);

    Device deleteDevice(String accountId, String deviceId);
}
