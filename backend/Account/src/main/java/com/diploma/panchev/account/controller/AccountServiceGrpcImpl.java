package com.diploma.panchev.account.controller;

import com.diploma.panchev.account.controller.mapper.GrpcMapper;
import com.diploma.panchev.account.domain.AccountNeedle;
import com.diploma.panchev.account.grpc.AccountGrpc;
import com.diploma.panchev.account.grpc.AccountServiceGrpc;
import com.diploma.panchev.account.service.AccountService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@GRpcService
public class AccountServiceGrpcImpl extends AccountServiceGrpc.AccountServiceImplBase {
    private final static GrpcMapper MAPPER = Mappers.getMapper(GrpcMapper.class);

    private final AccountService accountService;

    public AccountServiceGrpcImpl(AccountService accountService) {
        this.accountService = accountService;
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
        super.createDeviceGroup(request, responseObserver);
    }

    @Override
    public void getDeviceGroups(AccountGrpc.GetDeviceGroupsRequest request, StreamObserver<AccountGrpc.GetDeviceGroupsResponse> responseObserver) {
        super.getDeviceGroups(request, responseObserver);
    }

    @Override
    public void updateDeviceGroup(AccountGrpc.UpdateDeviceGroupRequest request, StreamObserver<AccountGrpc.UpdateDeviceGroupResponse> responseObserver) {
        super.updateDeviceGroup(request, responseObserver);
    }

    @Override
    public void getAccountDevice(AccountGrpc.GetAccountDeviceRequest request, StreamObserver<AccountGrpc.GetAccountDeviceResponse> responseObserver) {
        super.getAccountDevice(request, responseObserver);
    }

    @Override
    public void assignAccountDevice(AccountGrpc.AssignAccountDeviceRequest request, StreamObserver<AccountGrpc.AssignAccountDeviceResponse> responseObserver) {
        super.assignAccountDevice(request, responseObserver);
    }

    @Override
    public void createAccountDevice(AccountGrpc.CreateAccountDeviceRequest request, StreamObserver<AccountGrpc.CreateAccountDeviceResponse> responseObserver) {
        super.createAccountDevice(request, responseObserver);
    }

    @Override
    public void updateAccountDevice(AccountGrpc.UpdateAccountDeviceRequest request, StreamObserver<AccountGrpc.UpdateAccountDeviceResponse> responseObserver) {
        super.updateAccountDevice(request, responseObserver);
    }

    @Override
    public void getAccountDevices(AccountGrpc.GetAccountDevicesRequest request, StreamObserver<AccountGrpc.GetAccountDevicesResponse> responseObserver) {
        super.getAccountDevices(request, responseObserver);
    }

    @Override
    public void getDeviceGroupDevices(AccountGrpc.GetDeviceGroupDevicesRequest request, StreamObserver<AccountGrpc.GetDeviceGroupDevicesResponse> responseObserver) {
        super.getDeviceGroupDevices(request, responseObserver);
    }

    @Override
    public void deleteAccountDevice(AccountGrpc.DeleteAccountDeviceRequest request, StreamObserver<AccountGrpc.DeleteAccountDeviceResponse> responseObserver) {
        super.deleteAccountDevice(request, responseObserver);
    }

    @Override
    public void deleteDeviceGroup(AccountGrpc.DeleteDeviceGroupRequest request, StreamObserver<AccountGrpc.DeleteDeviceGroupResponse> responseObserver) {
        super.deleteDeviceGroup(request, responseObserver);
    }
}
