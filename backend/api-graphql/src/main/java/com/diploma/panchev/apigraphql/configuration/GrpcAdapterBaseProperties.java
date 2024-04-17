package com.diploma.panchev.apigraphql.configuration;

import lombok.Data;

@Data
public class GrpcAdapterBaseProperties {
    private String target;
    private boolean secure = false;
}
