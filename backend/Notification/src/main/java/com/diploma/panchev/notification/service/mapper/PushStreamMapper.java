package com.diploma.panchev.notification.service.mapper;

import com.diploma.panchev.notification.domain.PushStream;
import com.diploma.panchev.notification.domain.entity.PushStreamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PushStreamMapper {
    @Mapping(source = "id", target = "streamId")
    PushStream map(PushStreamEntity entity);
}
