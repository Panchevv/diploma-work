package com.diploma.panchev.measurement.mapper;

import com.diploma.panchev.measurement.domain.DeviceMessage;
import com.diploma.panchev.measurement.domain.MeasurementType;
import com.diploma.panchev.messaging.MeasurementUpdate;
import com.google.protobuf.StringValue;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper
public interface MessageReceiverMapper {
    DeviceMessage map(MeasurementUpdate.MeasurementUpdateMessage message);

    @ValueMapping(source = MappingConstants.ANY_REMAINING, target = MappingConstants.NULL)
    MeasurementType map(MeasurementUpdate.MeasurementUpdateMessage.MeasurementType message);

    default String map(StringValue value) {
        if (value.isInitialized()) {
            return value.getValue();
        }
        return null;
    }

    default OffsetDateTime map(Timestamp value) {
        if (value.isInitialized()) {
            return OffsetDateTime.ofInstant(
                    Instant.ofEpochSecond(value.getSeconds(), value.getNanos()),
                    ZoneOffset.UTC
            );
        }
        return null;
    }
}
