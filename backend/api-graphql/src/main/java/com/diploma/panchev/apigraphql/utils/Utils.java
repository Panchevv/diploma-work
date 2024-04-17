package com.diploma.panchev.apigraphql.utils;

import com.diploma.panchev.apigraphql.configuration.GrpcAdapterBaseProperties;
import io.grpc.ManagedChannel;
import io.grpc.netty.NettyChannelBuilder;

public class Utils {
    private Utils() { }

    public static ManagedChannel createManagedChannel(GrpcAdapterBaseProperties properties) {
        NettyChannelBuilder managedChannelBuilder = NettyChannelBuilder.forTarget(properties.getTarget())
                .enableRetry()
                .defaultLoadBalancingPolicy("round_robin");

        if (properties.isSecure()) {
            managedChannelBuilder.useTransportSecurity();
        } else {
            managedChannelBuilder.usePlaintext();
        }
        managedChannelBuilder.intercept(new GrpcClientLogger());
        return managedChannelBuilder.build();
    }
}
