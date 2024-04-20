package com.diploma.panchev.account.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class AccountGroupNeedle {
    private UUID accountId;
    private UUID deviceGroupId;
    private Boolean ceased;
}
