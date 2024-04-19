package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.Device;
import com.diploma.panchev.apigraphql.DeviceGroup;
import com.diploma.panchev.apigraphql.Notification;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
import com.diploma.panchev.apigraphql.domain.graphql.query.DeviceGroupInternal;
import com.diploma.panchev.apigraphql.service.DeviceService;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateDeviceGroup;
import graphql.schema.DataFetchingEnvironment;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetchersDelegateDeviceGroupImpl implements DataFetchersDelegateDeviceGroup {
    private static final GraphqlApiMapper MAPPER = Mappers.getMapper(GraphqlApiMapper.class);

    private final DeviceService deviceService;

    public DataFetchersDelegateDeviceGroupImpl(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Override
    public List<Device> devices(DataFetchingEnvironment dataFetchingEnvironment, DeviceGroup origin) {
        if (origin instanceof DeviceGroupInternal deviceGroup) {
            return this.deviceService.getAccountDevices(deviceGroup.getAccountId(), deviceGroup.getId())
                    .stream()
                    .map(MAPPER::map)
                    .toList();
        }
        return List.of();
    }

    @Override
    public Device device(DataFetchingEnvironment dataFetchingEnvironment, DeviceGroup origin, String id) {
        if (origin instanceof DeviceGroupInternal deviceGroup) {
            return this.deviceService.getAccountGroupDevice(deviceGroup.getAccountId(), deviceGroup.getId(), id)
                    .map(MAPPER::map)
                    .orElse(null);
        }
        return null;
    }

    @Override
    public List<Notification> notificationHistory(DataFetchingEnvironment dataFetchingEnvironment, DeviceGroup origin, String deviceId, String last, Integer pageSize) {
        return this.deviceService.getNotificationHistory(
                        origin.getId(),
                        pageSize,
                        last,
                        deviceId
                )
                .stream()
                .map(MAPPER::map)
                .toList();
    }
}
