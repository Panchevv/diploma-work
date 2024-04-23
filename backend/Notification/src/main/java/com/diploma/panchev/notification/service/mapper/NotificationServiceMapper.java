package com.diploma.panchev.notification.service.mapper;

import com.diploma.panchev.messaging.MeasurementUpdateInternal;
import com.diploma.panchev.messaging.events.EventsProto;
import com.diploma.panchev.notification.controller.mapper.ProtobufMapper;
import com.diploma.panchev.notification.domain.event.Event;
import com.diploma.panchev.notification.domain.event.MeasurementThreshold;
import com.diploma.panchev.notification.domain.event.entity.EventEntity;
import com.diploma.panchev.notification.domain.event.entity.ThresholdEntity;
import com.diploma.panchev.notification.domain.measurement.MeasurementType;
import com.diploma.panchev.notification.domain.measurement.MeasurementUpdate;
import com.diploma.panchev.notification.utils.Constants;
import com.google.protobuf.Timestamp;
import org.mapstruct.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        uses = {ProtobufMapper.class}
)
public interface NotificationServiceMapper {
    Event map(EventEntity entity);

    MeasurementThreshold map(ThresholdEntity measurement);

    MeasurementUpdate map(MeasurementUpdateInternal.UpdateMeasurementInternal message);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.NULL)
    MeasurementType map(MeasurementUpdateInternal.UpdateMeasurementInternal.MeasurementType type);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.NULL)
    Event map(EventsProto.EventMessage eventMessage);

    @Mapping(target = "id", ignore = true)
    ThresholdEntity map(MeasurementThreshold measurement);

    @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "threshold", ignore = true)
    EventEntity mapToEntity(Event event);

    default String map(UUID id) {
        return id.toString();
    }

    default OffsetDateTime map(Timestamp value) {
        if (value == null) {
            return null;
        }
        return OffsetDateTime.ofInstant(
                Instant.ofEpochSecond(value.getSeconds(), value.getNanos()),
                Constants.CURRENT_ZONE
        );
    }
}
