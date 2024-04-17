package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.Device;
import com.diploma.panchev.apigraphql.DeviceEdge;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateDeviceEdge;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

@Component
public class DataFetchersDelegateDeviceEdgeImpl implements DataFetchersDelegateDeviceEdge {
    @Override
    public Device node(DataFetchingEnvironment dataFetchingEnvironment, DeviceEdge origin) {
        return origin.getNode();
    }
}
