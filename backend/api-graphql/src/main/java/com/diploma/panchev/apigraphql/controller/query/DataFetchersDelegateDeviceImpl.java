package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.Device;
import com.diploma.panchev.apigraphql.DeviceMeasurement;
import com.diploma.panchev.apigraphql.MeasurementType;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateDevice;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class DataFetchersDelegateDeviceImpl implements DataFetchersDelegateDevice {
    private final static GraphqlApiMapper MAPPER = Mappers.getMapper(GraphqlApiMapper.class);

    @Override
    public CompletableFuture<DeviceMeasurement> measurement(DataFetchingEnvironment dataFetchingEnvironment, DataLoader<String, DeviceMeasurement> dataLoader, Device origin, MeasurementType type) {
        return null;
    }

    @Override
    public DeviceMeasurement measurement(DataFetchingEnvironment dataFetchingEnvironment, Device origin, MeasurementType type) {
        return null;
    }
}
