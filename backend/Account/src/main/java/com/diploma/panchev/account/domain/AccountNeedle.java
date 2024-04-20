package com.diploma.panchev.account.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class AccountNeedle {
    private boolean inactive;
    private UUID accountId;
    private String userId;
}
