package com.diploma.panchev.apigraphql.adapter.notification.mapper;

import com.diploma.panchev.apigraphql.domain.*;
import com.diploma.panchev.apigraphql.utils.ProtobufMapper;
import com.diploma.panchev.notification.grpc.NotificationGrpc;
import com.diploma.panchev.notification.history.grpc.NotificationHistoryGrpc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

import java.util.Arrays;

@Mapper(uses = {ProtobufMapper.class})
public interface NotificationMapper {
    @Mapping(source = "streamId", target = "token")
    SubscriptionSession map(NotificationGrpc.CreateStreamResponse response);

    @Mapping(source = "triggeredAt", target = "when")
    Notification map(NotificationHistoryGrpc.Event event);

    @Mapping(source = "value", target = "measurementValue")
    @Mapping(source = "type", target = "measurementType")
    NotificationThreshold map(NotificationHistoryGrpc.MeasurementThreshold threshold);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.NULL)
    @Mapping(source = "measurement.type", target = "type")
    @Mapping(source = "measurement.value", target = "value")
    @Mapping(source = "createdAt", target = "when")
    Measurement map(NotificationGrpc.UpdatedMeasurementMessage update);

    @Mapping(source = "triggeredAt", target = "when")
    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.NULL)
    Notification map(NotificationGrpc.UpdatedEventMessage update);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.NULL)
    @Mapping(source = "type", target = "measurementType")
    @Mapping(source = "value", target = "measurementValue")
    NotificationThreshold map(NotificationGrpc.UpdatedEventMessage.MeasurementThreshold measurementThreshold);

    default NotificationHistoryGrpc.MeasurementType map(MeasurementType type) {
        if(type == null) {
            return NotificationHistoryGrpc.MeasurementType.UNRECOGNIZED;
        }
        return Arrays.stream(NotificationHistoryGrpc.MeasurementType.values())
                .filter(value -> value.name().equals(type.name()))
                .findAny()
                .orElse(null);
    }

    default NotificationHistoryGrpc.OperationType map(ThresholdOperator type) {
        if(type == null) {
            return NotificationHistoryGrpc.OperationType.UNRECOGNIZED;
        }
        return Arrays.stream(NotificationHistoryGrpc.OperationType.values())
                .filter(value -> value.name().equals(type.name()))
                .findAny()
                .orElse(null);
    }

    default ThresholdOperator map(NotificationHistoryGrpc.OperationType type) {
        if(type == null) {
            return null;
        }
        return Arrays.stream(ThresholdOperator.values())
                .filter(value -> value.name().equals(type.name()))
                .findAny()
                .orElse(null);
    }

    default MeasurementType map(NotificationHistoryGrpc.MeasurementType type) {
        if(type == null) {
            return null;
        }
        return Arrays.stream(MeasurementType.values())
                .filter(value -> value.name().equals(type.name()))
                .findAny()
                .orElse(null);
    }
}
