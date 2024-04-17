package com.diploma.panchev.apigraphql.adapter.account.mapper;

import com.diploma.panchev.account.grpc.AccountGrpc;
import com.diploma.panchev.apigraphql.domain.Account;
import com.diploma.panchev.apigraphql.utils.ProtobufMapper;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        uses = {ProtobufMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AccountMapper {
    Account map(AccountGrpc.Account entity);
}
