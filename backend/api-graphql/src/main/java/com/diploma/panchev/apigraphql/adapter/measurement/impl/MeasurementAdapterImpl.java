package com.diploma.panchev.apigraphql.adapter.measurement.impl;

import com.diploma.panchev.account.grpc.AccountGrpc;
import com.diploma.panchev.apigraphql.adapter.measurement.MeasurementAdapter;
import com.diploma.panchev.apigraphql.adapter.measurement.mapper.MeasurementMapper;
import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;
import com.diploma.panchev.measurement.grpc.MeasurementGrpc;
import com.diploma.panchev.measurement.grpc.ThresholdServiceGrpc;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Threshold editThreshold(String id, ThresholdNeedle threshold) {
        return MAPPER.map(
                this.thresholdGrpc.editThreshold(MAPPER.mapEdit(id, threshold)).getThreshold()
        );
    }

    @Override
    public Threshold deleteThreshold(String id) {
        return MAPPER.map(
                this.thresholdGrpc.deleteThreshold(
                        MeasurementGrpc.DeleteThresholdRequest.newBuilder()
                                .setId(id)
                                .build()
                ).getThreshold()
        );
    }

    @Override
    public List<Threshold> getThresholds(String accountId) {
        return Optional.ofNullable(
                        this.thresholdGrpc.getThresholds(
                                MeasurementGrpc.GetThresholdsRequest.newBuilder()
                                        .setAccountId(accountId)
                                        .build()
                        )
                )
                .stream()
                .flatMap(response -> response.getThresholdsList().stream())
                .map(MAPPER::map)
                .collect(Collectors.toList());
    }
}
