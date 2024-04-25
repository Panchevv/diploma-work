package com.diploma.panchev.nrfcloud.controller;

import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice;

@Slf4j
@GRpcServiceAdvice
public class GrpcExceptionHandler {
    @GRpcExceptionHandler
    public Status handler(Throwable exc, GRpcExceptionScope scope) {
        log.error("Exception when executing: {}", exc.getMessage(), exc);
        return Status.fromThrowable(exc);
    }
}
