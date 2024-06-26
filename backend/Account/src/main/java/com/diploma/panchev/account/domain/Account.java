package com.diploma.panchev.account.domain;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class Account {
    private UUID accountId;
    private String name;
    private OffsetDateTime ceasedOn;
}
