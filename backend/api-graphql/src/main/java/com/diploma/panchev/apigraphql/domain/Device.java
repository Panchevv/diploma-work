package com.diploma.panchev.apigraphql.domain;

import lombok.Data;

import java.util.List;

@Data
public class Device {
    private String id;
    private String deviceId;
    private String name;
    private String accountId;
    private String groupId;
    private List<Measurement> measurements;

    public String getCursor() {
        return this.deviceId;
    }
}
