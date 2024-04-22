package com.diploma.panchev.measurement.service.mapper;

import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.entity.DeviceMeasurementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MeasurementMapper {

    @Mapping(source = "id", target = "measurementId")
    @Mapping(source = "type", target = "measurement.type")
    @Mapping(source = "value", target = "measurement.value")
    @Mapping(source = "createdAt", target = "measurement.when")
    DeviceMeasurement map(DeviceMeasurementEntity entity);
}
