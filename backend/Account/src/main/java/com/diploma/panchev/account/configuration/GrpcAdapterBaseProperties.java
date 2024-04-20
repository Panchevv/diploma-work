package com.diploma.panchev.account.configuration;

import lombok.Data;

@Data
public class GrpcAdapterBaseProperties {
    private String target;
    private boolean secure = false;
}
