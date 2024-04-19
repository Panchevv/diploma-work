package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.*;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
import com.diploma.panchev.apigraphql.service.DeviceService;
import com.diploma.panchev.apigraphql.service.MeasurementService;
import com.diploma.panchev.apigraphql.service.NrfCloudService;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateAccount;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class DataFetchersDelegateAccountImpl implements DataFetchersDelegateAccount {
    private final static GraphqlApiMapper MAPPER = Mappers.getMapper(GraphqlApiMapper.class);

    private final DeviceService deviceService;
    private final MeasurementService measurementService;
    private final NrfCloudService nrfCloudService;

    public DataFetchersDelegateAccountImpl(DeviceService deviceService, MeasurementService measurementService, NrfCloudService nrfCloudService) {
        this.deviceService = deviceService;
        this.measurementService = measurementService;
        this.nrfCloudService = nrfCloudService;
    }

    @Override
    public CompletableFuture<DeviceGroup> deviceGroup(DataFetchingEnvironment dataFetchingEnvironment, DataLoader<String, DeviceGroup> dataLoader, Account origin, String id) {
        return CompletableFuture.supplyAsync(() -> this.deviceGroup(dataFetchingEnvironment, origin, id));
    }

    @Override
    public DeviceGroup deviceGroup(DataFetchingEnvironment dataFetchingEnvironment, Account origin, String id) {
        return this.deviceService.getDeviceGroup(origin.getId(), id)
                .map(current -> (com.diploma.panchev.apigraphql.DeviceGroup) MAPPER.map(current))
                .orElse(null);
    }

    @Override
    public List<DeviceGroup> deviceGroups(DataFetchingEnvironment dataFetchingEnvironment, Account origin) {
        return this.deviceService.getDeviceGroups(origin.getId()).stream()
                .map(current -> (com.diploma.panchev.apigraphql.DeviceGroup) MAPPER.map(current))
                .toList();
    }

    @Override
    public DeviceConnection devices(DataFetchingEnvironment dataFetchingEnvironment, Account origin, DeviceSearch search, String after, Integer first) {
        return MAPPER.mapDevices(
                this.deviceService.getAccountDevices(origin.getId(), search.getUngrouped(), after, first == null ? 30 : first)
        );
    }

    @Override
    public Device device(DataFetchingEnvironment dataFetchingEnvironment, Account origin, String id) {
        return this.deviceService.getAccountDevice(origin.getId(), id)
                .map(MAPPER::map)
                .orElse(null);
    }

    @Override
    public List<Threshold> thresholds(DataFetchingEnvironment dataFetchingEnvironment, Account origin) {
        return this.measurementService.getThresholds(origin.getId())
                .stream()
                .map(MAPPER::map)
                .toList();
    }

    @Override
    public NrfAccountSettings nrfCloudSettings(DataFetchingEnvironment dataFetchingEnvironment, Account origin) {
        return MAPPER.map(
                this.nrfCloudService.getAccountSettings(origin.getId())
        );
    }
}
