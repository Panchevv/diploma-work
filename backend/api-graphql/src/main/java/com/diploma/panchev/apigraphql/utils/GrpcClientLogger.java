package com.diploma.panchev.apigraphql.utils;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.UUID;

@Slf4j
public class GrpcClientLogger implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            final MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {
        String correlationId = UUID.randomUUID().toString();
        long start = Instant.now().toEpochMilli();
        return new BackendForwardingClientCall<>(method, next.newCall(method, callOptions)) {
            @Override
            public void sendMessage(ReqT message) {
                log.info("Outgoing Request: {}\nMethod: {}\nRequest:\n{}", correlationId, methodName, message);
                super.sendMessage(message);
            }

            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                super.start(new BackendListener<>(correlationId, methodName, start, responseListener), headers);
            }
        };
    }

    private static class BackendListener<ReqT> extends ClientCall.Listener<ReqT> {
        private final String correlationId;
        private final String methodName;
        private final long start;
        private final ClientCall.Listener<ReqT> responseListener;

        protected BackendListener(String correlationId, String methodName, long start, ClientCall.Listener<ReqT> responseListener) {
            super();
            this.correlationId = correlationId;
            this.methodName = methodName;
            this.start = start;
            this.responseListener = responseListener;
        }

        @Override
        public void onMessage(ReqT message) {
            long end = Instant.now().toEpochMilli();
            log.info("Incoming Response: {}\nDuration: {} ms\nMethod: {}\nResponse:\n{}", correlationId, end - start, methodName, message);
            responseListener.onMessage(message);
        }

        @Override
        public void onHeaders(Metadata headers) {
            responseListener.onHeaders(headers);
        }

        @Override
        public void onClose(Status status, Metadata trailers) {
            log.info("Close: {}\nMethod: {}\nStatus:\n{}", correlationId, methodName, status);
            responseListener.onClose(status, trailers);
        }

        @Override
        public void onReady() {
            responseListener.onReady();
        }
    }

    private static class BackendForwardingClientCall<ReqT, RespT> extends io.grpc.ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT> {
        String methodName;

        protected BackendForwardingClientCall(MethodDescriptor<ReqT, RespT> method, ClientCall<ReqT, RespT> delegate) {
            super(delegate);
            methodName = method.getFullMethodName();
        }
    }
}
