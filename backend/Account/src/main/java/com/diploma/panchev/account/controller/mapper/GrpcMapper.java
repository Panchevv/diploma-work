package com.diploma.panchev.account.controller.mapper;

import com.diploma.panchev.account.domain.Account;
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
}
