package com.diploma.panchev.measurement.controller;

import com.diploma.panchev.measurement.controller.mapper.GrpcMapper;
import com.diploma.panchev.measurement.grpc.MeasurementGrpc;
import com.diploma.panchev.measurement.grpc.ThresholdServiceGrpc;
import com.diploma.panchev.measurement.service.ThresholdService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@GRpcService
public class ThresholdGrpcServiceImpl extends ThresholdServiceGrpc.ThresholdServiceImplBase{
    private final static GrpcMapper MAPPER = Mappers.getMapper(GrpcMapper.class);

    private final ThresholdService thresholdService;

    public ThresholdGrpcServiceImpl(ThresholdService thresholdService) {
        this.thresholdService = thresholdService;
    }

    @Override
    public void createThreshold(MeasurementGrpc.CreateThresholdRequest request, StreamObserver<MeasurementGrpc.CreateThresholdResponse> responseObserver) {
        if (request.getGroupIdsCount() == 0) {
            responseObserver.onError(
                    new Throwable("group_ids can not be empty")
            );
        } else {
            responseObserver.onNext(
                    MeasurementGrpc.CreateThresholdResponse.newBuilder()
                            .setThreshold(
                                    MAPPER.map(this.thresholdService.createThreshold(MAPPER.map(request)))
                            )
                            .build()
            );
        }
        responseObserver.onCompleted();
    }

    @Override
    public void editThreshold(MeasurementGrpc.EditThresholdsRequest request, StreamObserver<MeasurementGrpc.EditThresholdsResponse> responseObserver) {
        responseObserver.onNext(
                MeasurementGrpc.EditThresholdsResponse.newBuilder()
                        .setThreshold(
                                MAPPER.map(
                                        this.thresholdService.editThreshold(
                                                UUID.fromString(request.getId()),
                                                MAPPER.map(request)
                                        )
                                )
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void deleteThreshold(MeasurementGrpc.DeleteThresholdRequest request, StreamObserver<MeasurementGrpc.DeleteThresholdResponse> responseObserver) {
        responseObserver.onNext(
                MeasurementGrpc.DeleteThresholdResponse.newBuilder()
                        .setThreshold(
                                MAPPER.map(
                                        this.thresholdService.removeThreshold(UUID.fromString(request.getId()))
                                )
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getThresholds(MeasurementGrpc.GetThresholdsRequest request, StreamObserver<MeasurementGrpc.GetThresholdsResponse> responseObserver) {
        responseObserver.onNext(
                MeasurementGrpc.GetThresholdsResponse.newBuilder()
                        .addAllThresholds(
                                this.thresholdService.getThresholds(
                                                request.getAccountId()
                                        )
                                        .stream()
                                        .map(MAPPER::map)
                                        .toList()
                        ).build()
        );
        responseObserver.onCompleted();
    }
}
