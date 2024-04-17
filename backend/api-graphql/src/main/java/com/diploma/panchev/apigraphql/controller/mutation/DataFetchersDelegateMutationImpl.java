package com.diploma.panchev.apigraphql.controller.mutation;

import com.diploma.panchev.apigraphql.*;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
import com.diploma.panchev.apigraphql.service.AccountService;
import com.diploma.panchev.apigraphql.service.NrfCloudService;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateMutation;
import graphql.schema.DataFetchingEnvironment;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class DataFetchersDelegateMutationImpl implements DataFetchersDelegateMutation {
    private static final GraphqlApiMapper MAPPER = Mappers.getMapper(GraphqlApiMapper.class);

    private final AccountService accountService;
    private final NrfCloudService nrfCloudService;

    public DataFetchersDelegateMutationImpl(AccountService accountService, NrfCloudService nrfCloudService) {
        this.accountService = accountService;
        this.nrfCloudService = nrfCloudService;
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
    public Threshold createThreshold(DataFetchingEnvironment dataFetchingEnvironment, String accountId, AgentSensorThresholdRequest request) {
        return null;
    }

    @Override
    public Threshold editThreshold(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String id, AgentSensorThresholdRequest request) {
        return null;
    }

    @Override
    public Threshold deleteThreshold(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String id) {
        return null;
    }

    @Override
    public DeviceGroup createDeviceGroup(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String name) {
        return null;
    }

    @Override
    public DeviceGroup updateDeviceGroup(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String groupId, GroupUpdate update) {
        return null;
    }

    @Override
    public SubscriptionSession createDeviceGroupSubscription(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String groupId) {
        return null;
    }

    @Override
    public DeviceGroup assignDeviceDeviceGroup(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String deviceId, String groupId) {
        return null;
    }

    @Override
    public Device createDevice(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String deviceId, String groupId, DeviceRequest request) {
        return null;
    }

    @Override
    public Device updateDevice(DataFetchingEnvironment dataFetchingEnvironment, String accountId, String deviceId, DeviceUpdate update) {
        return null;
    }
}
