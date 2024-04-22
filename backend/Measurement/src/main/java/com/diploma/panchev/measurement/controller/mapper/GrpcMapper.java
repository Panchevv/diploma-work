package com.diploma.panchev.measurement.controller.mapper;

import com.diploma.panchev.measurement.domain.*;
import com.diploma.panchev.measurement.domain.entity.DeviceMeasurementEntity;
import com.diploma.panchev.measurement.grpc.MeasurementGrpc;
import org.mapstruct.*;

@Mapper(
        uses = {ProtobufMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface GrpcMapper {
    @Mapping(source = "createdAt", target = "when")
    MeasurementGrpc.Measurement map(DeviceMeasurementEntity entity);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.NULL)
    MeasurementType map(MeasurementGrpc.MeasurementType measurementType);

    @Mapping(source = "items", target = "measurementsList")
    MeasurementGrpc.GetMeasurementHistoryResponse mapHistory(Relay<DeviceMeasurementEntity> relay);

    @Mapping(source = "groupIdsList", target = "groupIds")
    ThresholdRequest map(MeasurementGrpc.CreateThresholdRequest request);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.NULL)
    ThresholdOperator map(MeasurementGrpc.OperationType operationType);

    @Mapping(source = "groupIds", target = "groupIdsList")
    MeasurementGrpc.Threshold map(Threshold threshold);

    @Mapping(source = "groupIdsList", target = "groupIds")
    ThresholdRequest map(MeasurementGrpc.EditThresholdsRequest request);
}
