package com.diploma.panchev.apigraphql.controller.mutation;

import com.diploma.panchev.apigraphql.*;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
import com.diploma.panchev.apigraphql.service.AccountService;
import com.diploma.panchev.apigraphql.service.DeviceService;
import com.diploma.panchev.apigraphql.service.MeasurementService;
import com.diploma.panchev.apigraphql.service.NrfCloudService;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateMutation;
import graphql.schema.DataFetchingEnvironment;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DataFetchersDelegateMutationImpl implements DataFetchersDelegateMutation {
    private static final GraphqlApiMapper MAPPER = Mappers.getMapper(GraphqlApiMapper.class);

    private final AccountService accountService;
    private final NrfCloudService nrfCloudService;
    private final MeasurementService measurementService;
    private final DeviceService deviceService;

    public DataFetchersDelegateMutationImpl(AccountService accountService, NrfCloudService nrfCloudService, MeasurementService measurementService, DeviceService deviceService) {
        this.accountService = accountService;
        this.nrfCloudService = nrfCloudService;
        this.measurementService = measurementService;
        this.deviceService = deviceService;
    }

    @Override
    public Account createAccount(DataFetchingEnvironment dataFetchingEnvironment, String name) {
        return MAPPER.map(
                this.accountService.createAccount(name)
        );
    }

    @Override
    public NrfAccountSettings setNrfCloudAccount(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String bearerToken) {
        return MAPPER.map(
                this.nrfCloudService.setNrfCloudAccount(
                        accountId,
                        bearerToken
                )
        );
    }

    @Override
    public Threshold createThreshold(DataFetchingEnvironment dataFetchingEnvironment, String accountId, ThresholdRequest request) {
        return this.accountService.getAccount(accountId)
                .map(account ->
                        this.measurementService.createThreshold(account.getId(), MAPPER.map(request))
                )
                .map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Wrong accountId passed"));
    }

    @Override
    public Threshold editThreshold(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String id, ThresholdRequest request) {
        return this.accountService.getAccount(accountId)
                .map(account ->
                        this.measurementService.editThreshold(id, MAPPER.map(request))
                )
                .map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Wrong accountId passed"));
    }

    @Override
    public Threshold deleteThreshold(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String id) {
        return this.accountService.getAccount(accountId)
                .map(account ->
                        this.measurementService.deleteThreshold(id)
                )
                .map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Wrong accountId passed"));
    }

    @Override
    public DeviceGroup createDeviceGroup(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String name) {
        return MAPPER.map(
                this.deviceService.createDeviceGroup(accountId, name)
        );
    }

    @Override
    public DeviceGroup updateDeviceGroup(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String groupId, GroupUpdate update) {
        return this.deviceService.getDeviceGroup(accountId, groupId)
                .map(deviceGroup ->
                        this.deviceService.updateDeviceGroup(deviceGroup.getAccountId(), deviceGroup.getId(), update.getName())
                )
                .map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Wrong accountId and groupId passed"));
    }

    @Override
    public SubscriptionSession createDeviceGroupSubscription(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String groupId) {
        return this.deviceService.getDeviceGroup(accountId, groupId)
                .map(deviceGroup ->
                        this.deviceService.generateSubscriptionSession(
                                deviceGroup.getAccountId(),
                                deviceGroup.getId()
                        )
                )
                .map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Wrong accountId and groupId passed"));
    }

    @Override
    public DeviceGroup assignDeviceDeviceGroup(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String deviceId, String groupId) {
        return this.deviceService.getAccountDevice(accountId, deviceId)
                .flatMap(device ->
                        this.deviceService.getDeviceGroup(accountId, groupId)
                )
                .map(group -> {
                    this.deviceService.assignDeviceGroup(accountId, group.getId(), deviceId);
                    return group;
                })
                .map(MAPPER::map)
                .orElseThrow();
    }

    @Override
    public Device createDevice(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String deviceId, String groupId, DeviceRequest request) {
        return this.accountService.getAccount(accountId)
                .map(account ->
                        this.deviceService.createDevice(
                                account.getId(),
                                groupId,
                                deviceId,
                                request.getName()
                        )
                ).map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Wrong accountId passed"));
    }

    @Override
    public Device updateDevice(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String deviceId, DeviceUpdate update) {
        return this.deviceService.getAccountDevice(accountId, deviceId)
                .map(device ->
                        this.deviceService.updateDevice(
                                accountId,
                                device.getId(),
                                update.getName()
                        )
                ).map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Wrong accountId and deviceId passed"));
    }

    @Override
    public DeviceGroup deleteDeviceGroup(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String groupId) {
        return this.deviceService.getDeviceGroup(accountId, groupId)
                .map(deviceGroup ->
                        this.deviceService.deleteDeviceGroup(deviceGroup.getAccountId(), deviceGroup.getId())
                )
                .map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Wrong accountId and groupId passed"));
    }

    @Override
    public Device deleteDevice(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String deviceId) {
        return this.deviceService.getAccountDevice(accountId, deviceId)
                .map(device ->
                        this.deviceService.deleteDevice(accountId, device.getId())
                )
                .map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Wrong accountId and deviceId passed"));
    }
}
