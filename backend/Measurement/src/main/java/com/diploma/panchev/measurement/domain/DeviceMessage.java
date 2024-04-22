package com.diploma.panchev.measurement.domain;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class DeviceMessage {
    private String messageId;
    private String accountId;
    private String groupId;
    private List<Measurement> measurementList;
    private OffsetDateTime createdAt;
}
