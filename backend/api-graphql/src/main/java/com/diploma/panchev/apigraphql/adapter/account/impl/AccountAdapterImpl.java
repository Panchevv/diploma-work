package com.diploma.panchev.apigraphql.adapter.account.impl;

import com.diploma.panchev.account.grpc.AccountGrpc;
import com.diploma.panchev.account.grpc.AccountServiceGrpc;
import com.diploma.panchev.apigraphql.adapter.account.AccountAdapter;
import com.diploma.panchev.apigraphql.adapter.account.mapper.AccountMapper;
import com.diploma.panchev.apigraphql.domain.Account;
import com.diploma.panchev.apigraphql.domain.Device;
import com.diploma.panchev.apigraphql.domain.DeviceGroup;
import com.google.protobuf.StringValue;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AccountAdapterImpl implements AccountAdapter {
    private final static AccountMapper MAPPER = Mappers.getMapper(AccountMapper.class);
    private final AccountServiceGrpc.AccountServiceBlockingStub grpcApi;

    public AccountAdapterImpl(AccountServiceGrpc.AccountServiceBlockingStub grpcApi) {
        this.grpcApi = grpcApi;
    }

    @Override
    public Account createAccount(String name, String userId) {
        return MAPPER.map(
                this.grpcApi.createAccount(
                        AccountGrpc.CreateAccountRequest.newBuilder()
                                .setName(name)
                                .setUserId(userId)
                                .build()
                ).getAccount()
        );
    }

    @Override
    public List<Account> getAccounts(String userId) {
        return Optional.ofNullable(
                        this.grpcApi.getAccounts(
                                AccountGrpc.GetAccountsRequest.newBuilder()
                                        .setUserId(StringValue.of(userId))
                                        .build()
                        )
                )
                .stream()
                .flatMap(response -> response.getAccountsList().stream())
                .map(MAPPER::map)
                .collect(Collectors.toList());
    }

    @Override
    public DeviceGroup createDeviceGroup(String accountId, String name) {
        return MAPPER.map(
                this.grpcApi.createDeviceGroup(
                        AccountGrpc.CreateDeviceGroupRequest.newBuilder()
                                .setAccountId(accountId)
                                .setName(name)
                                .build()
                ).getDeviceGroup()
        );
    }

    @Override
    public Optional<DeviceGroup> getDeviceGroup(String accountId, String deviceGroupId) {
        return Optional.ofNullable(
                        this.grpcApi.getDeviceGroups(
                                AccountGrpc.GetDeviceGroupsRequest.newBuilder()
                                        .setAccountId(accountId)
                                        .setDeviceGroupId(StringValue.of(deviceGroupId))
                                        .build()
                        )
                )
                .flatMap(response -> response.getDeviceGroupsCount() != 1 ?
                        Optional.empty() : Optional.of(response.getDeviceGroups(0)))
                .map(MAPPER::map);
    }

    @Override
    public DeviceGroup updateDeviceGroup(String accountId, String deviceGroupId, String name) {
        return Optional.ofNullable(
                        this.grpcApi.updateDeviceGroup(
                                AccountGrpc.UpdateDeviceGroupRequest.newBuilder()
                                        .setAccountId(accountId)
                                        .setGroupId(deviceGroupId)
                                        .setUpdate(
                                                AccountGrpc.DeviceGroupUpdate.newBuilder()
                                                        .setName(name)
                                        )
                                        .build()
                        )
                )
                .map(AccountGrpc.UpdateDeviceGroupResponse::getDeviceGroup)
                .map(MAPPER::map)
                .orElseThrow();
    }

    @Override
    public Optional<Device> getAccountDevice(String accountId, String deviceId) {
        return Optional.ofNullable(
                        this.grpcApi.getAccountDevice(
                                AccountGrpc.GetAccountDeviceRequest.newBuilder()
                                        .setAccountId(accountId)
                                        .setDeviceId(deviceId)
                                        .build()
                        )
                )
                .filter(AccountGrpc.GetAccountDeviceResponse::hasDevice)
                .map(AccountGrpc.GetAccountDeviceResponse::getDevice)
                .map(MAPPER::map);
    }

    @Override
    public Device assignDevice(String accountId, String groupId, String deviceId) {
        return MAPPER.map(
                this.grpcApi.assignAccountDevice(
                        AccountGrpc.AssignAccountDeviceRequest.newBuilder()
                                .setAccountId(accountId)
                                .setGroupId(groupId)
                                .setDeviceId(deviceId)
                                .buildPartial()
                ).getDevice()
        );
    }

    @Override
    public Device createAccountDevice(String accountId, String deviceId, String name) {
        return MAPPER.map(
                this.grpcApi.createAccountDevice(
                        AccountGrpc.CreateAccountDeviceRequest.newBuilder()
                                .setAccountId(accountId)
                                .setDeviceId(deviceId)
                                .setName(name)
                                .build()
                ).getDevice()
        );
    }

    @Override
    public Device updateDevice(String accountId, String deviceId, String name) {
        return MAPPER.map(
                this.grpcApi.updateAccountDevice(
                        AccountGrpc.UpdateAccountDeviceRequest.newBuilder()
                                .setAccountId(accountId)
                                .setDeviceId(deviceId)
                                .setUpdate(AccountGrpc.DeviceUpdate.newBuilder().setName(name))
                                .build()
                ).getDevice()
        );
    }
}
