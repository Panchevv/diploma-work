package com.diploma.panchev.nrfcloud.service.mapper;

import com.diploma.panchev.messaging.MeasurementUpdate;
import com.diploma.panchev.nrfcloud.domain.Measurement;
import com.google.protobuf.DoubleValue;
import com.google.protobuf.Int64Value;
import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper(collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface NotificationMapper {
    MeasurementUpdate.MeasurementUpdateMessage map(String messageId, String accountId, String deviceId, String groupId, List<Measurement> measurementList, OffsetDateTime createdAt);

    default StringValue map(String value) {
        if (value ==null || value.isBlank()) {
            return StringValue.newBuilder().build();
        }
        return StringValue.of(value);
    }
    default DoubleValue map(Double value) {
        if (value ==null) {
            return DoubleValue.newBuilder().build();
        }
        return DoubleValue.of(value);
    }

    default Timestamp map(OffsetDateTime value) {
        if (value == null) {
            return Timestamp.newBuilder().build();
        }
        return Timestamp.newBuilder()
                .setSeconds(value.toEpochSecond())
                .setNanos(value.getNano())
                .build();
    }

    default Int64Value map(Long value) {
        if (value == null) {
            return Int64Value.newBuilder().build();
        }
        return Int64Value.of(value);
    }
}
