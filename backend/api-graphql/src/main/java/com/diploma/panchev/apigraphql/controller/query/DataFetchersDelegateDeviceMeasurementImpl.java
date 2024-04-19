package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.DeviceMeasurement;
import com.diploma.panchev.apigraphql.MeasurementConnection;
import com.diploma.panchev.apigraphql.MeasurementHistorySearch;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
import com.diploma.panchev.apigraphql.service.MeasurementService;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateDeviceMeasurement;
import graphql.schema.DataFetchingEnvironment;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class DataFetchersDelegateDeviceMeasurementImpl implements DataFetchersDelegateDeviceMeasurement {
    private final static GraphqlApiMapper MAPPER = Mappers.getMapper(GraphqlApiMapper.class);
    private final MeasurementService measurementService;

    public DataFetchersDelegateDeviceMeasurementImpl(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @Override
    public MeasurementConnection history(DataFetchingEnvironment dataFetchingEnvironment, DeviceMeasurement origin, MeasurementHistorySearch search, String after, Integer first) {
        String fromId = null;
        if (after != null) {
            String[] parts = after.split("#");
            fromId = parts[2];
        }
        return this.measurementService.getDeviceMeasurementHistory(
                        origin.getDeviceId(),
                        MAPPER.map(origin.getType()),
                        search.getFrom(),
                        search.getTo(),
                        fromId,
                        first == null ? 30 : first
                )
                .map(MAPPER::mapMeasurements)
                .orElse(null);
    }
}
