package com.diploma.panchev.account.domain;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class Account {
    private Long accountId;
    private String name;
    private OffsetDateTime ceasedOn;
}
