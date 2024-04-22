package com.diploma.panchev.measurement.service.mapper;

import com.diploma.panchev.measurement.controller.mapper.ProtobufMapper;
import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.ThresholdHistory;
import com.diploma.panchev.messaging.MeasurementUpdateInternal;
import com.diploma.panchev.messaging.events.EventsProto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.OffsetDateTime;

@Mapper(
        uses = {ProtobufMapper.class},
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface NotificationMapper {
    @Mapping(target = "allFields", ignore = true)
    @Mapping(target = "unknownFields", ignore = true)
    @Mapping(target = "threshold", source = "measurementThreshold")
    EventsProto.EventMessage map(
            String id, String groupId, String deviceId, String referenceId, String name,
            EventsProto.EventMessage.MeasurementThreshold measurementThreshold,
            OffsetDateTime triggeredAt
    );

    @Mapping(source = "measurement.type", target = "type")
    @Mapping(source = "measurement.value", target = "value")
    @Mapping(source = "threshold.value", target = "threshold")
    @Mapping(source = "threshold.operator", target = "operator")
    EventsProto.EventMessage.MeasurementThreshold map(ThresholdHistory history);

    @Mapping(source = "measurementId", target = "id")
    MeasurementUpdateInternal.UpdateMeasurementInternal mapInternal(DeviceMeasurement measurement);
}
