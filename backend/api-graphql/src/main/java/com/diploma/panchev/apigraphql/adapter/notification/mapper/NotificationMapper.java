package com.diploma.panchev.apigraphql.adapter.notification.mapper;

import com.diploma.panchev.apigraphql.domain.SubscriptionSession;
import com.diploma.panchev.apigraphql.utils.ProtobufMapper;
import com.diploma.panchev.notification.grpc.NotificationGrpc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {ProtobufMapper.class})
public interface NotificationMapper {
    @Mapping(source = "streamId", target = "token")
    SubscriptionSession map(NotificationGrpc.CreateStreamResponse response);
}
