package com.diploma.panchev.account.controller.mapper;

import com.diploma.panchev.account.domain.*;
import com.diploma.panchev.account.grpc.AccountGrpc;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        uses = {ProtobufMapper.class},
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

    @Mapping(source = "items", target = "devicesList")
    AccountGrpc.GetAccountDevicesResponse map(Relay<AccountDevice> device);
}
