package com.diploma.panchev.apigraphql.adapter.measurement.impl;

import com.diploma.panchev.account.grpc.Common;
import com.diploma.panchev.apigraphql.adapter.measurement.MeasurementAdapter;
import com.diploma.panchev.apigraphql.adapter.measurement.mapper.MeasurementMapper;
import com.diploma.panchev.apigraphql.domain.Measurement;
import com.diploma.panchev.apigraphql.domain.MeasurementType;
import com.diploma.panchev.apigraphql.domain.Threshold;
import com.diploma.panchev.apigraphql.domain.ThresholdNeedle;
import com.diploma.panchev.apigraphql.domain.graphql.query.Connection;
import com.diploma.panchev.apigraphql.domain.graphql.query.PageInfo;
import com.diploma.panchev.apigraphql.utils.ProtobufMapper;
import com.diploma.panchev.measurement.grpc.MeasurementGrpc;
import com.diploma.panchev.measurement.grpc.MeasurementServiceGrpc;
import com.diploma.panchev.measurement.grpc.ThresholdServiceGrpc;
import com.google.protobuf.StringValue;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MeasurementAdapterImpl implements MeasurementAdapter {
    private static final ProtobufMapper PROTO_MAPPER = Mappers.getMapper(ProtobufMapper.class);
    private static final MeasurementMapper MAPPER = Mappers.getMapper(MeasurementMapper.class);
    private final ThresholdServiceGrpc.ThresholdServiceBlockingStub thresholdGrpc;
    private final MeasurementServiceGrpc.MeasurementServiceBlockingStub measurementGrpc;

    public MeasurementAdapterImpl(ThresholdServiceGrpc.ThresholdServiceBlockingStub thresholdGrpc, MeasurementServiceGrpc.MeasurementServiceBlockingStub measurementGrpc) {
        this.thresholdGrpc = thresholdGrpc;
        this.measurementGrpc = measurementGrpc;
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

    @Override
    public Optional<Measurement> getDeviceMeasurement(String deviceId, MeasurementType type) {
        return Optional.ofNullable(
                        this.measurementGrpc.getLastMeasurements(
                                MeasurementGrpc.GetLastMeasurementsRequest.newBuilder()
                                        .setDeviceId(StringValue.of(deviceId))
                                        .addType(MAPPER.map(type))
                                        .build()
                        )
                )
                .stream()
                .flatMap(r -> r.getMeasurementsList().stream())
                .findAny()
                .map(current -> MAPPER.map(deviceId, current));
    }

    @Override
    public List<Measurement> getDeviceMeasurements(String deviceId) {
        return Optional.ofNullable(
                        this.measurementGrpc.getLastMeasurements(
                                MeasurementGrpc.GetLastMeasurementsRequest.newBuilder()
                                        .setDeviceId(StringValue.of(deviceId))
                                        .build()
                        )
                )
                .stream()
                .flatMap(r -> r.getMeasurementsList().stream())
                .map(current -> MAPPER.map(deviceId, current))
                .toList();
    }

    @Override
    public Optional<Connection<Measurement>> getDeviceMeasurementHistory(String deviceId, MeasurementType type, OffsetDateTime from, OffsetDateTime to, String fromId, int pageSize) {
        Common.Pagination.Builder paginationBuilder = Common.Pagination.newBuilder().setSize(pageSize);
        if (fromId != null) {
            paginationBuilder.setFromId(StringValue.of(fromId));
        }

        return Optional.ofNullable(this.measurementGrpc.getMeasurementHistory(
                        MeasurementGrpc.GetMeasurementHistoryRequest.newBuilder()
                                .setDeviceId(StringValue.of(deviceId))
                                .setType(MAPPER.map(type))
                                .setCreatedAtFrom(PROTO_MAPPER.map(from))
                                .setCreatedAtTo(PROTO_MAPPER.map(to))
                                .setPagination(paginationBuilder)
                                .build()
                ))
                .map(response -> {
                    Connection<Measurement> history = new Connection<>();
                    PageInfo pageInfo = new PageInfo();
                    history.setEdges(
                            response.getMeasurementsList()
                                    .stream()
                                    .map(current -> MAPPER.map(deviceId, current))
                                    .toList()
                    );
                    if (history.getEdges() != null && !history.getEdges().isEmpty()) {
                        pageInfo.setStartCursor(history.getEdges().get(0).getCursor());
                        pageInfo.setEndCursor(history.getEdges().get(history.getEdges().size() - 1).getCursor());
                        pageInfo.setHasNextPage(response.getPageInfo().getHasNextPage());
                        pageInfo.setHasPreviousPage(response.getPageInfo().getHasPreviousPage());
                        pageInfo.setCount(response.getPageInfo().getCount());
                        pageInfo.setTotalElements(response.getPageInfo().hasTotalElements() ? response.getPageInfo().getTotalElements().getValue() : null);
                    }
                    history.setPageInfo(pageInfo);
                    return history;
                });
    }
}
