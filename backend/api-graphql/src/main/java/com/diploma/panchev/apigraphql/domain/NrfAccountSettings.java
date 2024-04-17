package com.diploma.panchev.apigraphql.domain;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class NrfAccountSettings {
    private String accountId;
    private Boolean active;
    private String bearerToken;
    private String deviceId;
    private OffsetDateTime modifiedAt;
    private OffsetDateTime createdAt;

    private String clientId;
}
