package com.diploma.panchev.notification.controller.mapper;

import com.diploma.panchev.notification.domain.event.Event;
import com.diploma.panchev.notification.domain.event.MeasurementType;
import com.diploma.panchev.notification.domain.event.ThresholdOperator;
import com.diploma.panchev.notification.domain.measurement.MeasurementUpdate;
import com.diploma.panchev.notification.grpc.NotificationGrpc;
import com.diploma.panchev.notification.history.grpc.NotificationHistoryGrpc;
import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import org.mapstruct.*;

import java.time.OffsetDateTime;
import java.util.Arrays;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        uses = {ProtobufMapper.class}
)
public interface ControllerMapper {
    NotificationGrpc.UpdatedMeasurementMessage map(MeasurementUpdate update);

    NotificationGrpc.UpdatedEventMessage map(Event event);

    @Mapping(source = "createdAt", target = "triggeredAt")
    NotificationHistoryGrpc.Event mapNotificationEvents(Event event);

    default StringValue map(String value){
        if (value == null){
            return null;
        }
        return StringValue.newBuilder().setValue(value).build();
    }

    default Timestamp map(OffsetDateTime value){
        if (value == null){
            return null;
        }
        return Timestamp.newBuilder()
                .setSeconds(value.toEpochSecond())
                .setNanos(value.getNano())
                .build();
    }

    default ThresholdOperator map(NotificationHistoryGrpc.OperationType type) {
        return Arrays.stream(ThresholdOperator.values())
                .filter(value -> value.name().equals(type.name()))
                .findAny()
                .orElse(null);
    }

    default MeasurementType map(NotificationHistoryGrpc.MeasurementType type) {
        return Arrays.stream(MeasurementType.values())
                .filter(value -> value.name().equals(type.name()))
                .findAny()
                .orElse(null);
    }
}
