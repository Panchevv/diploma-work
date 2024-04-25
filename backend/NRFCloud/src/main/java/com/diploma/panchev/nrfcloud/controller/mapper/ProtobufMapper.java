package com.diploma.panchev.nrfcloud.controller.mapper;

import com.google.protobuf.*;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ProtobufMapper {
    default Timestamp map(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return Timestamp.newBuilder().build();
        }
        return Timestamp.newBuilder()
                .setSeconds(offsetDateTime.toEpochSecond())
                .setNanos(offsetDateTime.getNano())
                .build();
    }

    default OffsetDateTime map(Timestamp timestamp) {
        if (timestamp == null || !timestamp.isInitialized()) {
            return null;
        }
        return OffsetDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()),
                ZoneId.systemDefault()
        );
    }

    default StringValue map(String value) {
        if (value == null) {
            return StringValue.newBuilder().build();
        }
        return StringValue.of(value);
    }

    default String map(StringValue value) {
        if (value == null || !value.isInitialized()) {
            return null;
        }
        return value.getValue();
    }

    default DoubleValue map(Double value) {
        if (value == null) {
            return DoubleValue.newBuilder().build();
        }
        return DoubleValue.of(value);
    }

    default Double map(DoubleValue value) {
        if (value == null || !value.isInitialized()) {
            return null;
        }
        return value.getValue();
    }

    default Int64Value map(Long value) {
        if (value == null) {
            return Int64Value.newBuilder().build();
        }
        return Int64Value.of(value);
    }

    default Long map(Int64Value value) {
        if (value == null || !value.isInitialized()) {
            return null;
        }
        return value.getValue();
    }

    default Int32Value map(Integer value) {
        if (value == null) {
            return Int32Value.newBuilder().build();
        }
        return Int32Value.of(value);
    }

    default Integer map(Int32Value value) {
        if (value == null || !value.isInitialized()) {
            return null;
        }
        return value.getValue();
    }
}
