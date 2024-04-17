package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.DeviceMeasurement;
import com.diploma.panchev.apigraphql.MeasurementConnection;
import com.diploma.panchev.apigraphql.MeasurementHistorySearch;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateDeviceMeasurement;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

@Component
public class DataFetchersDelegateDeviceMeasurementImpl implements DataFetchersDelegateDeviceMeasurement {
    @Override
    public MeasurementConnection history(DataFetchingEnvironment dataFetchingEnvironment, DeviceMeasurement origin, MeasurementHistorySearch search, String after, Integer first) {
        return null;
    }
}
