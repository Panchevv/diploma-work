package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.Device;
import com.diploma.panchev.apigraphql.DeviceGroup;
import com.diploma.panchev.apigraphql.Notification;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateDeviceGroup;
import graphql.schema.DataFetchingEnvironment;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetchersDelegateDeviceGroupImpl implements DataFetchersDelegateDeviceGroup {
    private static final GraphqlApiMapper MAPPER = Mappers.getMapper(GraphqlApiMapper.class);

    @Override
    public List<Device> devices(DataFetchingEnvironment dataFetchingEnvironment, DeviceGroup origin) {
        return List.of();
    }

    @Override
    public Device device(DataFetchingEnvironment dataFetchingEnvironment, DeviceGroup origin, String id) {
        return null;
    }

    @Override
    public List<Notification> notificationHistory(DataFetchingEnvironment dataFetchingEnvironment, DeviceGroup origin, String deviceId, String last, Integer pageSize) {
        return List.of();
    }
}
