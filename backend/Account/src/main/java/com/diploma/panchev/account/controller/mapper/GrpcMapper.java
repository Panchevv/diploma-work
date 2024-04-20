package com.diploma.panchev.account.controller.mapper;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.AccountDevice;
import com.diploma.panchev.account.domain.AccountGroup;
import com.diploma.panchev.account.domain.AccountGroupDevice;
import com.diploma.panchev.account.grpc.AccountGrpc;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GrpcMapper {
    @Mapping(source = "accountId", target = "id")
    AccountGrpc.Account map(Account account);

    @Mapping(source = "groupId", target = "id")
    AccountGrpc.DeviceGroup map(AccountGroup account);

    @Mapping(source = "accountDeviceId", target = "id")
    AccountGrpc.AccountDevice map(AccountDevice device);

    AccountGrpc.AccountDevice map(AccountGroupDevice device);
}
