package com.diploma.panchev.measurement.controller;

import com.diploma.panchev.measurement.controller.mapper.GrpcMapper;
import com.diploma.panchev.measurement.controller.mapper.ProtobufMapper;
import com.diploma.panchev.measurement.grpc.MeasurementGrpc;
import com.diploma.panchev.measurement.grpc.MeasurementServiceGrpc;
import com.diploma.panchev.measurement.service.MeasurementService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.mapstruct.factory.Mappers;

@GRpcService
public class MeasurementGrpcServiceImpl extends MeasurementServiceGrpc.MeasurementServiceImplBase {
    private final static GrpcMapper MAPPER = Mappers.getMapper(GrpcMapper.class);
    private final static ProtobufMapper PROTOBUF_MAPPER = Mappers.getMapper(ProtobufMapper.class);

    private final MeasurementService measurementService;

    public MeasurementGrpcServiceImpl(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @Override
    public void getLastMeasurements(MeasurementGrpc.GetLastMeasurementsRequest request, StreamObserver<MeasurementGrpc.GetLastMeasurementsResponse> responseObserver) {
        if (request.getTypeCount() > 0) {
            responseObserver.onNext(
                    MeasurementGrpc.GetLastMeasurementsResponse.newBuilder()
                            .addAllMeasurements(
                                    request.getTypeList()
                                            .stream()
                                            .map(MAPPER::map)
                                            .flatMap(measurementType ->
                                                    this.measurementService.getLastMeasurement(request.getDeviceId().getValue(), measurementType).stream()
                                            )
                                            .map(MAPPER::map)
                                            .toList()
                            )
                            .build()
            );
        } else {
            responseObserver.onNext(
                    MeasurementGrpc.GetLastMeasurementsResponse.newBuilder()
                            .addAllMeasurements(
                                    this.measurementService.getLastMeasurements(request.getDeviceId().getValue())
                                            .stream().map(MAPPER::map).toList()
                            )
                            .build()
            );
        }

        responseObserver.onCompleted();
    }

    @Override
    public void getMeasurementHistory(MeasurementGrpc.GetMeasurementHistoryRequest request, StreamObserver<MeasurementGrpc.GetMeasurementHistoryResponse> responseObserver) {
        responseObserver.onNext(
                MAPPER.mapHistory(
                        this.measurementService.getMeasurementHistory(request.getDeviceId().getValue(), MAPPER.map(request.getType()), PROTOBUF_MAPPER.map(request.getCreatedAtFrom()), PROTOBUF_MAPPER.map(request.getCreatedAtTo()),
                                request.hasPagination() && request.getPagination().hasFromId() ? Long.parseLong(request.getPagination().getFromId().getValue()) : null,
                                request.hasPagination() ? request.getPagination().getSize() : 30
                        )
                )
        );
        responseObserver.onCompleted();
    }
}
