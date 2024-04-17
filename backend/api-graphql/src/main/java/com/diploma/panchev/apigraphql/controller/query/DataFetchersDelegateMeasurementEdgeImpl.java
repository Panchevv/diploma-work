package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.Measurement;
import com.diploma.panchev.apigraphql.MeasurementEdge;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateMeasurementEdge;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class DataFetchersDelegateMeasurementEdgeImpl implements DataFetchersDelegateMeasurementEdge {
    @Override
    public CompletableFuture<Measurement> node(DataFetchingEnvironment dataFetchingEnvironment, DataLoader<String, Measurement> dataLoader, MeasurementEdge origin) {
        return CompletableFuture.supplyAsync(() -> this.node(dataFetchingEnvironment, origin));
    }

    @Override
    public Measurement node(DataFetchingEnvironment dataFetchingEnvironment, MeasurementEdge origin) {
        return origin.getNode();
    }
}
