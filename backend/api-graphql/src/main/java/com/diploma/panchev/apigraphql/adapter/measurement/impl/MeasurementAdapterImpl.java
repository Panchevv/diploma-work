package com.diploma.panchev.apigraphql.adapter.measurement.impl;

import com.diploma.panchev.apigraphql.adapter.measurement.MeasurementAdapter;
import com.diploma.panchev.apigraphql.adapter.measurement.mapper.MeasurementMapper;
import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;
import com.diploma.panchev.measurement.grpc.ThresholdServiceGrpc;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Component
public class MeasurementAdapterImpl implements MeasurementAdapter {
    private static final MeasurementMapper MAPPER = Mappers.getMapper(MeasurementMapper.class);
    private final ThresholdServiceGrpc.ThresholdServiceBlockingStub thresholdGrpc;

    public MeasurementAdapterImpl(ThresholdServiceGrpc.ThresholdServiceBlockingStub thresholdGrpc) {
        this.thresholdGrpc = thresholdGrpc;
    }

    @Override
    public Threshold createThreshold(String accountId, ThresholdNeedle threshold) {
        return MAPPER.map(
                this.thresholdGrpc.createThreshold(MAPPER.map(accountId, threshold)).getThreshold()
        );
    }
}
