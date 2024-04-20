package com.diploma.panchev.apigraphql.controller.subsription;

import com.diploma.panchev.apigraphql.Measurement;
import com.diploma.panchev.apigraphql.Notification;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
import com.diploma.panchev.apigraphql.service.DeviceService;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateSubscription;
import graphql.schema.DataFetchingEnvironment;
import org.mapstruct.factory.Mappers;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetchersDelegateSubscriptionImpl implements DataFetchersDelegateSubscription {
    private static final GraphqlApiMapper MAPPER = Mappers.getMapper(GraphqlApiMapper.class);
    private final DeviceService deviceService;

    public DataFetchersDelegateSubscriptionImpl(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public Publisher<List<Measurement>> measurements(DataFetchingEnvironment dataFetchingEnvironment, String token) {
        return this.deviceService.getGroupMeasurementUpdates(token)
                .map(list -> list.stream().map(MAPPER::mapMeasurement).toList());
    }

    @Override
    public Publisher<List<Notification>> notifications(DataFetchingEnvironment dataFetchingEnvironment, String token) {
        return this.deviceService.getGroupNotificationUpdates(token)
                .map(list -> list.stream().map(MAPPER::mapNotification).toList());
    }
}
