package com.diploma.panchev.nrfcloud.controller.mapper;

import com.diploma.panchev.nrf.grpc.NrfCloudGrpc;
import com.diploma.panchev.nrfcloud.domain.entity.ApiConfiguration;
import com.diploma.panchev.nrfcloud.domain.entity.MqttConfiguration;
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
    @Mapping(target = "accountId", source = "id")
    @Mapping(target = "active", source = "isActive")
    NrfCloudGrpc.AccountSettings map(ApiConfiguration apiConfiguration);

    NrfCloudGrpc.MqttSettings map(MqttConfiguration mqttConfiguration);
}
