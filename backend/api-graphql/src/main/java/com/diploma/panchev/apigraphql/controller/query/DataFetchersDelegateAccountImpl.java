package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.*;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
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

    @Override
    public CompletableFuture<DeviceGroup> deviceGroup(DataFetchingEnvironment dataFetchingEnvironment, DataLoader<String, DeviceGroup> dataLoader, Account origin, String id) {
        return null;
    }

    @Override
    public DeviceGroup deviceGroup(DataFetchingEnvironment dataFetchingEnvironment, Account origin, String id) {
        return null;
    }

    @Override
    public List<DeviceGroup> deviceGroups(DataFetchingEnvironment dataFetchingEnvironment, Account origin) {
        return List.of();
    }

    @Override
    public DeviceConnection devices(DataFetchingEnvironment dataFetchingEnvironment, Account origin, DeviceSearch search, String after, Integer first) {
        return null;
    }

    @Override
    public Device device(DataFetchingEnvironment dataFetchingEnvironment, Account origin, String id) {
        return null;
    }

    @Override
    public List<Threshold> thresholds(DataFetchingEnvironment dataFetchingEnvironment, Account origin) {
        return List.of();
    }

    @Override
    public NrfAccountSettings nrfCloudSettings(DataFetchingEnvironment dataFetchingEnvironment, Account origin) {
        return null;
    }
}
