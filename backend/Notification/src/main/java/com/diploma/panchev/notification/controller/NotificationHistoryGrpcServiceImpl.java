package com.diploma.panchev.notification.controller;

import com.diploma.panchev.notification.controller.mapper.ControllerMapper;
import com.diploma.panchev.notification.history.grpc.NotificationHistoryGrpc;
import com.diploma.panchev.notification.history.grpc.NotificationHistoryServiceGrpc;
import com.diploma.panchev.notification.service.NotificationService;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.mapstruct.factory.Mappers;

@GRpcService
public class NotificationHistoryGrpcServiceImpl extends NotificationHistoryServiceGrpc.NotificationHistoryServiceImplBase {
    private final static ControllerMapper MAPPER = Mappers.getMapper(ControllerMapper.class);
    private final NotificationService notificationService;

    public NotificationHistoryGrpcServiceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public void getNotificationHistory(NotificationHistoryGrpc.GetNotificationHistoryRequest request, StreamObserver<NotificationHistoryGrpc.GetNotificationHistoryResponse> responseObserver) {
        responseObserver.onNext(
                NotificationHistoryGrpc.GetNotificationHistoryResponse.newBuilder()
                        .setPageSize(request.getPageSize())
                        .setLast(request.getLast())
                        .addAllEvents(
                                notificationService.getNotificationHistory(request.getGroupId(), request.getDeviceId().getValue(), request.getLast().getValue(), request.getPageSize().getValue())
                                        .stream()
                                        .map(MAPPER::mapNotificationEvents)
                                        .toList()
                        )
                        .build()
        );
        responseObserver.onCompleted();
    }
}
