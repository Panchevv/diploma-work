package com.diploma.panchev.notification.domain;

import java.time.OffsetDateTime;

public record PushStream(String streamId, String groupId, OffsetDateTime expiresAt) {
}
