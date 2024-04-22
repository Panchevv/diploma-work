package com.diploma.panchev.measurement.service.mapper;

import com.diploma.panchev.measurement.domain.Threshold;
import com.diploma.panchev.measurement.domain.ThresholdHistory;
import com.diploma.panchev.measurement.domain.entity.ThresholdEntity;
import com.diploma.panchev.measurement.domain.entity.ThresholdHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ThresholdMapper {
    @Mapping(source = "type", target = "measurementType")
    Threshold map(ThresholdEntity entity);

    @Mapping(source = "entity.type", target = "measurement.type")
    @Mapping(source = "entity.actual", target = "measurement.value")
    @Mapping(source = "entity.createdAt", target = "measurement.when")
    @Mapping(source = "entity.createdAt", target = "when")
    ThresholdHistory map(String groupId, ThresholdHistoryEntity entity);
}
