package com.diploma.panchev.account.domain;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class AccountDevice {
    private UUID accountDeviceId;
    private String deviceId;
    private String name;
    private UUID accountId;
    private UUID groupId;
    private AccountGroup group;
    private Account account;
    private OffsetDateTime createdOn;
}
