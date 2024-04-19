package com.diploma.panchev.apigraphql.adapter.notification.mapper;

import com.diploma.panchev.apigraphql.domain.MeasurementType;
import com.diploma.panchev.apigraphql.domain.Notification;
import com.diploma.panchev.apigraphql.domain.SubscriptionSession;
import com.diploma.panchev.apigraphql.domain.ThresholdOperator;
import com.diploma.panchev.apigraphql.utils.ProtobufMapper;
import com.diploma.panchev.notification.grpc.NotificationGrpc;
import com.diploma.panchev.notification.history.grpc.NotificationHistoryGrpc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;

@Mapper(uses = {ProtobufMapper.class})
public interface NotificationMapper {
    @Mapping(source = "streamId", target = "token")
    SubscriptionSession map(NotificationGrpc.CreateStreamResponse response);

    @Mapping(source = "triggeredAt", target = "when")
    Notification map(NotificationHistoryGrpc.Event event);

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
