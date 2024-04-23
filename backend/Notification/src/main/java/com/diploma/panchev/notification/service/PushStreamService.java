package com.diploma.panchev.notification.service;

import com.diploma.panchev.notification.domain.PushStream;

import java.util.Optional;

public interface PushStreamService {
    PushStream createPushStream(String accountId, String groupId);

    Optional<PushStream> getPushStream(String streamId);
}
