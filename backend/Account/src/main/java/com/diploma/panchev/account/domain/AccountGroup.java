package com.diploma.panchev.account.domain;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class AccountGroup {
    private UUID groupId;
    private OffsetDateTime ceasedOn;
    private Account account;
    private String name;
}
