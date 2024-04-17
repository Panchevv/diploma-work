package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.MeasurementConnection;
import com.diploma.panchev.apigraphql.MeasurementEdge;
import com.diploma.panchev.apigraphql.PageInfo;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateMeasurementConnection;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetchersDelegateMeasurementConnectionImpl implements DataFetchersDelegateMeasurementConnection {
    @Override
    public PageInfo pageInfo(DataFetchingEnvironment dataFetchingEnvironment, MeasurementConnection origin) {
        return origin.getPageInfo();
    }

    @Override
    public List<MeasurementEdge> edges(DataFetchingEnvironment dataFetchingEnvironment, MeasurementConnection origin) {
        return origin.getEdges();
    }
}
