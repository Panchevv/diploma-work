package com.diploma.panchev.account.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class AccountGroupDeviceNeedle {
    private UUID accountId;
    private UUID deviceGroupId;
    private String deviceId;
}
