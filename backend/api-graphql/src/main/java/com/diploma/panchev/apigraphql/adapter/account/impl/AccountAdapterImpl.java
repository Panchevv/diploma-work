package com.diploma.panchev.apigraphql.adapter.account.impl;

import com.diploma.panchev.account.grpc.AccountGrpc;
import com.diploma.panchev.account.grpc.AccountServiceGrpc;
import com.diploma.panchev.account.grpc.Common;
import com.diploma.panchev.apigraphql.adapter.account.AccountAdapter;
import com.diploma.panchev.apigraphql.adapter.account.mapper.AccountMapper;
import com.diploma.panchev.apigraphql.domain.Account;
import com.diploma.panchev.apigraphql.domain.Device;
import com.diploma.panchev.apigraphql.domain.DeviceGroup;
import com.diploma.panchev.apigraphql.domain.graphql.query.Connection;
import com.diploma.panchev.apigraphql.domain.graphql.query.PageInfo;
import com.google.protobuf.BoolValue;
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

    @Override
    public List<DeviceGroup> getDeviceGroups(String accountId) {
        return Optional.ofNullable(
                        this.grpcApi.getDeviceGroups(
                                AccountGrpc.GetDeviceGroupsRequest.newBuilder()
                                        .setAccountId(accountId)
                                        .build()
                        )
                )
                .stream()
                .flatMap(response -> response.getDeviceGroupsList().stream())
                .map(MAPPER::map)
                .collect(Collectors.toList());
    }

    @Override
    public Connection<Device> getAccountDevices(String accountId, Boolean ungrouped, String fromDeviceId, int pageSize) {
        Common.Pagination.Builder paginationBuilder = Common.Pagination.newBuilder().setSize(pageSize);
        if (fromDeviceId != null) {
            paginationBuilder.setFromId(StringValue.of(fromDeviceId));
        }

        return Optional.ofNullable(
                        this.grpcApi.getAccountDevices(
                                AccountGrpc.GetAccountDevicesRequest.newBuilder()
                                        .setAccountId(accountId)
                                        .setUngrouped(ungrouped != null ? BoolValue.of(ungrouped) : BoolValue.newBuilder().build())
                                        .build()
                        )
                )
                .map(response -> {
                    Connection<Device> history = new Connection<>();
                    PageInfo pageInfo = new PageInfo();
                    history.setEdges(
                            response.getDevicesList()
                                    .stream()
                                    .map(MAPPER::map)
                                    .toList()
                    );
                    if (history.getEdges() != null && !history.getEdges().isEmpty()) {
                        pageInfo.setStartCursor(history.getEdges().get(0).getCursor());
                        pageInfo.setEndCursor(history.getEdges().get(history.getEdges().size() - 1).getCursor());
                        pageInfo.setHasNextPage(true);
                    }
                    history.setPageInfo(pageInfo);
                    return history;

                })
                .orElseGet(Connection::new);
    }

    @Override
    public List<Device> getAccountGroupDevices(String accountId, String deviceGroupId) {
        return Optional.ofNullable(
                        this.grpcApi.getDeviceGroupDevices(
                                AccountGrpc.GetDeviceGroupDevicesRequest.newBuilder()
                                        .setAccountId(StringValue.of(accountId))
                                        .setDeviceGroupId(deviceGroupId)
                                        .build()
                        )
                )
                .stream()
                .flatMap(response -> response.getDevicesList().stream())
                .map(MAPPER::map)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Device> getAccountGroupDevice(String accountId, String deviceGroupId, String deviceId) {
        return Optional.ofNullable(
                        this.grpcApi.getDeviceGroupDevices(
                                AccountGrpc.GetDeviceGroupDevicesRequest.newBuilder()
                                        .setAccountId(StringValue.of(accountId))
                                        .setDeviceGroupId(deviceGroupId)
                                        .setDeviceId(StringValue.of(deviceId))
                                        .build()
                        )
                )
                .flatMap(response -> response.getDevicesCount() != 1 ?
                        Optional.empty() : Optional.of(response.getDevices(0)))
                .map(MAPPER::map);
    }

    @Override
    public Device deleteDevice(String accountId, String deviceId) {
        return MAPPER.map(
                this.grpcApi.deleteAccountDevice(
                        AccountGrpc.DeleteAccountDeviceRequest.newBuilder()
                                .setAccountId(accountId)
                                .setDeviceId(deviceId)
                                .build()
                ).getDevice()
        );
    }

    @Override
    public DeviceGroup deleteDeviceGroup(String accountId, String groupId) {
        return MAPPER.map(
                this.grpcApi.deleteDeviceGroup(
                        AccountGrpc.DeleteDeviceGroupRequest.newBuilder()
                                .setAccountId(accountId)
                                .setGroupId(groupId)
                                .build()
                ).getDeviceGroup()
        );
    }
}
