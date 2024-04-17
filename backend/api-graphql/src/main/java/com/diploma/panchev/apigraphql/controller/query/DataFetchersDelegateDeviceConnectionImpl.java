package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.DeviceConnection;
import com.diploma.panchev.apigraphql.DeviceEdge;
import com.diploma.panchev.apigraphql.PageInfo;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateDeviceConnection;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetchersDelegateDeviceConnectionImpl implements DataFetchersDelegateDeviceConnection {
    @Override
    public PageInfo pageInfo(DataFetchingEnvironment dataFetchingEnvironment, DeviceConnection origin) {
        return origin.getPageInfo();
    }

    @Override
    public List<DeviceEdge> edges(DataFetchingEnvironment dataFetchingEnvironment, DeviceConnection origin) {
        return origin.getEdges();
    }
}
