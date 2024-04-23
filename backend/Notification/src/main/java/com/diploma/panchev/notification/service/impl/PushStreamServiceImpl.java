package com.diploma.panchev.notification.service.impl;

import com.diploma.panchev.notification.domain.PushStream;
import com.diploma.panchev.notification.domain.entity.PushStreamEntity;
import com.diploma.panchev.notification.repository.PushStreamEntityRepository;
import com.diploma.panchev.notification.service.PushStreamService;
import com.diploma.panchev.notification.service.mapper.PushStreamMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PushStreamServiceImpl implements PushStreamService {
    private static final PushStreamMapper MAPPER = Mappers.getMapper(PushStreamMapper.class);
    private final PushStreamEntityRepository repository;

    public PushStreamServiceImpl(PushStreamEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public PushStream createPushStream(String accountId, String groupId) {
        PushStreamEntity entity = new PushStreamEntity();
        entity.setId(UUID.randomUUID());
        entity.setAccountId(accountId);
        entity.setGroupId(groupId);
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setExpiresAt(entity.getCreatedAt().plusHours(1));
        return MAPPER.map(this.repository.save(entity));
    }

    @Override
    public Optional<PushStream> getPushStream(String streamId) {
        return this.repository.findById(UUID.fromString(streamId))
                .filter(current -> current.getExpiresAt().isAfter(OffsetDateTime.now()))
                .map(MAPPER::map);
    }
}
