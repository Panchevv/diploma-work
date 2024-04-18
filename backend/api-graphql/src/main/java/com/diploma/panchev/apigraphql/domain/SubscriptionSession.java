package com.diploma.panchev.apigraphql.domain;

import java.time.OffsetDateTime;

public record SubscriptionSession(String token, OffsetDateTime expiresAt) {
}
