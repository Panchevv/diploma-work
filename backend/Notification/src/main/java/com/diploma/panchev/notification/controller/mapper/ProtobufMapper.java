package com.diploma.panchev.notification.controller.mapper;

import com.google.protobuf.*;
import org.mapstruct.Mapper;

@Mapper
public interface ProtobufMapper {
    default Double map(DoubleValue value) {
        if (value == null) {
            return null;
        }
        return value.getValue();
    }

    default Long map(Int64Value value) {
        if (value == null) {
            return null;
        }
        return value.getValue();
    }

    default String map(StringValue value){
        if (value == null){
            return null;
        }
        return value.getValue();
    }

    default Long map(UInt64Value value) {
        if (value == null) {
            return null;
        }
        return value.getValue();
    }

    default Long map(Timestamp value) {
        if (value != null) {
            return value.getSeconds();
        }
        return null;
    }

    default Timestamp map(Long value) {
        if (value != null) {
            return Timestamp.newBuilder().setSeconds(value).build();
        }
        return Timestamp.newBuilder().build();
    }

    default DoubleValue map(Double value) {
        if (value == null) {
            return DoubleValue.newBuilder().build();
        }
        return DoubleValue.newBuilder().setValue(value).build();
    }

    default Int64Value mapInt64(Long value) {
        if (value == null) {
            return Int64Value.newBuilder().build();
        }
        return Int64Value.newBuilder().setValue(value).build();
    }

    default Int32Value map(Integer value) {
        if (value == null) {
            return Int32Value.newBuilder().build();
        }
        return Int32Value.newBuilder().setValue(value).build();

    }

    default Integer map(Int32Value value) {
        if (value == null) {
            return null;
        }
        return value.getValue();
    }
}
