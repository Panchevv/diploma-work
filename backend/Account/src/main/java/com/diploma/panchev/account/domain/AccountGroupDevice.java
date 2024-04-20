package com.diploma.panchev.account.domain;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class AccountGroupDevice {
    private Long mappingId;
    private String deviceId;
    private String name;
    private UUID accountId;
    private UUID groupId;
    private Account account;
    private AccountGroup group;
    private OffsetDateTime createdOn;
    private OffsetDateTime activeTo;
    private OffsetDateTime activeFrom;
}
