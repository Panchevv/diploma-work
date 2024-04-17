package com.diploma.panchev.apigraphql.adapter.account.impl;

import com.diploma.panchev.account.grpc.AccountGrpc;
import com.diploma.panchev.account.grpc.AccountServiceGrpc;
import com.diploma.panchev.apigraphql.adapter.account.AccountAdapter;
import com.diploma.panchev.apigraphql.adapter.account.mapper.AccountMapper;
import com.diploma.panchev.apigraphql.domain.Account;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

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
}
