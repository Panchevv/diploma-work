package com.diploma.panchev.account.controller;

import com.diploma.panchev.account.controller.mapper.GrpcMapper;
import com.diploma.panchev.account.domain.AccountDeviceNeedle;
import com.diploma.panchev.account.domain.AccountGroupDeviceNeedle;
import com.diploma.panchev.account.domain.AccountGroupNeedle;
import com.diploma.panchev.account.domain.AccountNeedle;
import com.diploma.panchev.account.grpc.AccountGrpc;
import com.diploma.panchev.account.grpc.AccountServiceGrpc;
import com.diploma.panchev.account.service.AccountDeviceService;
import com.diploma.panchev.account.service.AccountGroupDeviceService;
import com.diploma.panchev.account.service.AccountGroupService;
import com.diploma.panchev.account.service.AccountService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.mapstruct.factory.Mappers;

import java.util.Objects;
import java.util.UUID;

@GRpcService
public class AccountServiceGrpcImpl extends AccountServiceGrpc.AccountServiceImplBase {
    private final static GrpcMapper MAPPER = Mappers.getMapper(GrpcMapper.class);

    private final AccountService accountService;
    private final AccountGroupService accountGroupService;
    private final AccountDeviceService accountDeviceService;
    private final AccountGroupDeviceService accountGroupDeviceService;

    public AccountServiceGrpcImpl(AccountService accountService, AccountGroupService accountGroupService, AccountDeviceService accountDeviceService, AccountGroupDeviceService accountGroupDeviceService) {
        this.accountService = accountService;
        this.accountGroupService = accountGroupService;
        this.accountDeviceService = accountDeviceService;
        this.accountGroupDeviceService = accountGroupDeviceService;
    }

    @Override
    public void createAccount(AccountGrpc.CreateAccountRequest request, StreamObserver<AccountGrpc.CreateAccountResponse> responseObserver) {
        responseObserver.onNext(
                AccountGrpc.CreateAccountResponse.newBuilder()
                        .setAccount(
                                MAPPER.map(
                                        this.accountService.createAccount(
                                                request.getName(),
                                                request.getUserId()
                                        )
                                )
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getAccounts(AccountGrpc.GetAccountsRequest request, StreamObserver<AccountGrpc.GetAccountsResponse> responseObserver) {
        responseObserver.onNext(
                AccountGrpc.GetAccountsResponse.newBuilder()
                        .addAllAccounts(
                                this.accountService.getAccounts(
                                                AccountNeedle.builder()
                                                        .accountId(request.hasAccountId() ? UUID.fromString(request.getAccountId().getValue()) : null)
                                                        .userId(request.hasUserId() ? request.getUserId().getValue() : null)
                                                        .inactive(false)
                                                        .build()
                                        )
                                        .stream()
                                        .map(MAPPER::map)
                                        .toList()
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void createDeviceGroup(AccountGrpc.CreateDeviceGroupRequest request, StreamObserver<AccountGrpc.CreateDeviceGroupResponse> responseObserver) {
        responseObserver.onNext(
                AccountGrpc.CreateDeviceGroupResponse.newBuilder()
                        .setDeviceGroup(
                                this.accountService.getAccount(UUID.fromString(request.getAccountId()))
                                        .filter(account -> Objects.isNull(account.getCeasedOn()))
                                        .map(account -> this.accountGroupService.createAccountGroup(
                                                        account,
                                                        request.getName()
                                                )
                                        )
                                        .map(MAPPER::map)
                                        .orElseGet(() -> AccountGrpc.DeviceGroup.newBuilder().build())
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getDeviceGroups(AccountGrpc.GetDeviceGroupsRequest request, StreamObserver<AccountGrpc.GetDeviceGroupsResponse> responseObserver) {
        responseObserver.onNext(
                AccountGrpc.GetDeviceGroupsResponse.newBuilder()
                        .addAllDeviceGroups(
                                this.accountGroupService.getAccountGroups(
                                                AccountGroupNeedle.builder()
                                                        .accountId(UUID.fromString(request.getAccountId()))
                                                        .deviceGroupId(request.hasDeviceGroupId() ? UUID.fromString(request.getDeviceGroupId().getValue()) : null)
                                                        .ceased(false)
                                                        .build()
                                        )
                                        .stream()
                                        .map(MAPPER::map)
                                        .toList()
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void updateDeviceGroup(AccountGrpc.UpdateDeviceGroupRequest request, StreamObserver<AccountGrpc.UpdateDeviceGroupResponse> responseObserver) {
        responseObserver.onNext(
                AccountGrpc.UpdateDeviceGroupResponse.newBuilder()
                        .setDeviceGroup(
                                MAPPER.map(
                                        this.accountGroupService.updateAccountGroup(
                                                UUID.fromString(request.getGroupId()),
                                                request.getUpdate().getName()
                                        )
                                )
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getAccountDevice(AccountGrpc.GetAccountDeviceRequest request, StreamObserver<AccountGrpc.GetAccountDeviceResponse> responseObserver) {
        responseObserver.onNext(
                this.accountDeviceService.getAccountDevice(UUID.fromString(request.getAccountId()), request.getDeviceId())
                        .map(MAPPER::map)
                        .map(device -> AccountGrpc.GetAccountDeviceResponse.newBuilder().setDevice(device).build())
                        .orElseGet(() -> AccountGrpc.GetAccountDeviceResponse.newBuilder().build())
        );
        responseObserver.onCompleted();
    }

    @Override
    public void assignAccountDevice(AccountGrpc.AssignAccountDeviceRequest request, StreamObserver<AccountGrpc.AssignAccountDeviceResponse> responseObserver) {
        responseObserver.onNext(
                AccountGrpc.AssignAccountDeviceResponse.newBuilder()
                        .setDevice(
                                MAPPER.map(
                                        this.accountGroupDeviceService.addAccountGroupDevice(
                                                UUID.fromString(request.getAccountId()),
                                                UUID.fromString(request.getGroupId()),
                                                request.getDeviceId()
                                        )
                                )
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void createAccountDevice(AccountGrpc.CreateAccountDeviceRequest request, StreamObserver<AccountGrpc.CreateAccountDeviceResponse> responseObserver) {
        responseObserver.onNext(
                AccountGrpc.CreateAccountDeviceResponse.newBuilder()
                        .setDevice(
                                this.accountService.getAccount(UUID.fromString(request.getAccountId()))
                                        .filter(account -> Objects.isNull(account.getCeasedOn()))
                                        .map(account -> accountDeviceService.addAccountDevice(account, request.getDeviceId(), request.getName()))
                                        .map(MAPPER::map)
                                        .orElseGet(() -> AccountGrpc.AccountDevice.newBuilder().build())
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void updateAccountDevice(AccountGrpc.UpdateAccountDeviceRequest request, StreamObserver<AccountGrpc.UpdateAccountDeviceResponse> responseObserver) {
        responseObserver.onNext(
                AccountGrpc.UpdateAccountDeviceResponse.newBuilder()
                        .setDevice(
                                MAPPER.map(
                                        this.accountDeviceService.updateAccountDevice(UUID.fromString(request.getAccountId()), request.getDeviceId(), request.getUpdate().getName())
                                )
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getAccountDevices(AccountGrpc.GetAccountDevicesRequest request, StreamObserver<AccountGrpc.GetAccountDevicesResponse> responseObserver) {
        responseObserver.onNext(
                MAPPER.map(
                        this.accountDeviceService.getDevices(
                                new AccountDeviceNeedle(
                                        UUID.fromString(request.getAccountId()),
                                        request.hasUngrouped() ? request.getUngrouped().getValue() : null
                                ),
                                request.hasPagination()
                                        && request.getPagination().hasFromId() ? request.getPagination().getFromId().getValue() : null,
                                request.hasPagination() ? request.getPagination().getSize() : 30
                        )
                )
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getDeviceGroupDevices(AccountGrpc.GetDeviceGroupDevicesRequest request, StreamObserver<AccountGrpc.GetDeviceGroupDevicesResponse> responseObserver) {
        responseObserver.onNext(
                AccountGrpc.GetDeviceGroupDevicesResponse.newBuilder()
                        .addAllDevices(
                                this.accountGroupDeviceService.getAccountGroupDevices(
                                                AccountGroupDeviceNeedle.builder()
                                                        .accountId(request.hasAccountId() ? UUID.fromString(request.getAccountId().getValue()) : null)
                                                        .deviceGroupId(UUID.fromString(request.getDeviceGroupId()))
                                                        .deviceId(request.hasDeviceId() ? request.getDeviceId().getValue() : null)
                                                        .build()
                                        )
                                        .stream()
                                        .map(MAPPER::map)
                                        .toList()
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void deleteAccountDevice(AccountGrpc.DeleteAccountDeviceRequest request, StreamObserver<AccountGrpc.DeleteAccountDeviceResponse> responseObserver) {
        this.accountService.getAccount(UUID.fromString(request.getAccountId()))
                .filter(account -> Objects.isNull(account.getCeasedOn()))
                .ifPresentOrElse(account -> {
                            responseObserver.onNext(
                                    AccountGrpc.DeleteAccountDeviceResponse.newBuilder()
                                            .setDevice(
                                                    MAPPER.map(
                                                            this.accountDeviceService.removeAccountDevice(account, request.getDeviceId())
                                                    )
                                            )
                                            .build()
                            );
                            responseObserver.onCompleted();
                        }
                        , () ->
                                responseObserver.onError(Status.NOT_FOUND.withDescription("Account is ceased or not found").asRuntimeException())
                );
    }

    @Override
    public void deleteDeviceGroup(AccountGrpc.DeleteDeviceGroupRequest request, StreamObserver<AccountGrpc.DeleteDeviceGroupResponse> responseObserver) {
        this.accountService.getAccount(UUID.fromString(request.getAccountId()))
                .ifPresentOrElse(account ->
                                this.accountGroupService.getAccountGroup(account, UUID.fromString(request.getGroupId()))
                                        .ifPresentOrElse(group -> {
                                                    responseObserver.onNext(
                                                            AccountGrpc.DeleteDeviceGroupResponse.newBuilder()
                                                                    .setDeviceGroup(
                                                                            MAPPER.map(
                                                                                    this.accountGroupService.removeAccountGroup(account, group)
                                                                            )
                                                                    )
                                                                    .build()
                                                    );
                                                    responseObserver.onCompleted();
                                                }
                                                , () ->
                                                        responseObserver.onError(Status.NOT_FOUND.withDescription("Group not found for the given account").asRuntimeException())
                                        )
                        , () ->
                                responseObserver.onError(Status.NOT_FOUND.withDescription("Account is not found").asRuntimeException())
                );
    }
}
