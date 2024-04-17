package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.Threshold;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateThreshold;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetchersDelegateThresholdImpl implements DataFetchersDelegateThreshold {
    @Override
    public List<String> groupIds(DataFetchingEnvironment dataFetchingEnvironment, Threshold origin) {
        return origin.getGroupIds();
    }
}
