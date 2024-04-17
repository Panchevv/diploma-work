package com.diploma.panchev.apigraphql.adapter.account.impl;

import com.diploma.panchev.account.grpc.AccountGrpc;
import com.diploma.panchev.account.grpc.AccountServiceGrpc;
import com.diploma.panchev.apigraphql.adapter.account.AccountAdapter;
import com.diploma.panchev.apigraphql.adapter.account.mapper.AccountMapper;
import com.diploma.panchev.apigraphql.domain.Account;
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
}
