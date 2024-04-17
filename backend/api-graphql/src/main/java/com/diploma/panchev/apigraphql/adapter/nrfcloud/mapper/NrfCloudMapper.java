package com.diploma.panchev.apigraphql.adapter.nrfcloud.mapper;

import com.diploma.panchev.apigraphql.domain.MqttSettings;
import com.diploma.panchev.apigraphql.domain.NrfAccountSettings;
import com.diploma.panchev.apigraphql.utils.ProtobufMapper;
import com.diploma.panchev.nrf.grpc.NrfCloudGrpc;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        uses = {ProtobufMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface NrfCloudMapper {
    NrfAccountSettings map(NrfCloudGrpc.AccountSettings accountSettings);

    MqttSettings map(NrfCloudGrpc.MqttSettings mqttSettings);
}
