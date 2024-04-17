package com.diploma.panchev.apigraphql.adapter.measurement.mapper;

import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;
import com.diploma.panchev.apigraphql.utils.ProtobufMapper;
import com.diploma.panchev.measurement.grpc.MeasurementGrpc;
import org.mapstruct.*;

@Mapper(
        uses = {ProtobufMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface MeasurementMapper {
    @Mapping(source = "threshold.groupIds", target = "groupIdsList")
    MeasurementGrpc.CreateThresholdRequest map(String accountId, ThresholdNeedle threshold);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.NULL)
    @Mapping(source = "groupIdsList", target = "groupIds")
    Threshold map(MeasurementGrpc.Threshold threshold);
}
