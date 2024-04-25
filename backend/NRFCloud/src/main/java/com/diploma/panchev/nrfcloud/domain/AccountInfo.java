package com.diploma.panchev.nrfcloud.domain;

import lombok.Data;

@Data
public class AccountInfo {
    private String mqttEndpoint;
    private String mqttTopicPrefix;
    private String tenantId;
}
