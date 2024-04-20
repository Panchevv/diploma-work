package com.diploma.panchev.account.service.impl;

import com.diploma.panchev.account.domain.AccountGroupDevice;
import com.diploma.panchev.account.domain.entity.AccountDeviceEntity;
import com.diploma.panchev.account.domain.entity.AccountGroupDeviceEntity;
import com.diploma.panchev.account.domain.entity.AccountGroupDeviceHistoryEntity;
import com.diploma.panchev.account.domain.entity.AccountGroupEntity;
import com.diploma.panchev.account.mapper.database.AccountGroupDeviceMapper;
import com.diploma.panchev.account.repository.AccountDeviceRepository;
import com.diploma.panchev.account.repository.AccountGroupDeviceHistoryRepository;
import com.diploma.panchev.account.repository.AccountGroupDeviceRepository;
import com.diploma.panchev.account.repository.AccountGroupRepository;
import com.diploma.panchev.account.service.AccountGroupDeviceService;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class AccountGroupDeviceServiceImpl implements AccountGroupDeviceService {
    private static final AccountGroupDeviceMapper MAPPER = Mappers.getMapper(AccountGroupDeviceMapper.class);

    private final AccountGroupDeviceHistoryRepository accountGroupDeviceHistoryRepository;
    private final AccountGroupDeviceRepository accountGroupDeviceRepository;
    private final AccountDeviceRepository accountDeviceRepository;
    private final AccountGroupRepository accountGroupRepository;

    public AccountGroupDeviceServiceImpl(AccountGroupDeviceHistoryRepository accountGroupDeviceHistoryRepository, AccountGroupDeviceRepository accountGroupDeviceRepository, AccountDeviceRepository accountDeviceRepository, AccountGroupRepository accountGroupRepository) {
        this.accountGroupDeviceHistoryRepository = accountGroupDeviceHistoryRepository;
        this.accountGroupDeviceRepository = accountGroupDeviceRepository;
        this.accountDeviceRepository = accountDeviceRepository;
        this.accountGroupRepository = accountGroupRepository;
    }

    @Override
    public AccountGroupDevice addAccountGroupDevice(@NonNull UUID accountId, @NonNull UUID groupId, @NonNull String deviceId) {
        log.trace("addAccountGroupDevice: accountId={} groupId={} deviceId={}", accountId, groupId, deviceId);
        AccountDeviceEntity accountDeviceEntity = this.accountDeviceRepository.findByAccountIdAndDeviceId(accountId, deviceId)
                .orElseThrow();

        AccountGroupEntity groupEntity = this.accountGroupRepository.findById(groupId)
                .orElseThrow();

        Optional<AccountGroupDeviceEntity> optMapping = this.accountGroupDeviceRepository.findByAccountDeviceId(accountDeviceEntity.getId());
        OffsetDateTime now = OffsetDateTime.now();

        AccountGroupDeviceEntity  mapping = null;
        if (optMapping.isPresent()) {
            mapping = optMapping.get();
            if (mapping.getGroup().getId().equals(groupEntity.getId())) {
                return MAPPER.map(mapping);
            }

            this.accountGroupDeviceHistoryRepository.findActiveByDeviceId(deviceId)
                    .ifPresent(historyEntity -> {
                        historyEntity.setActiveTo(now);
                        historyEntity.setModifiedOn(now);
                        this.accountGroupDeviceHistoryRepository.save(historyEntity);
                    });

        } else {
            mapping = new AccountGroupDeviceEntity();
            mapping.setAccountDevice(accountDeviceEntity);
        }

        mapping.setGroup(groupEntity);

        if (mapping.getCreatedAt() == null) {
            mapping.setCreatedAt(now);
        } else {
            mapping.setModifiedAt(now);
        }

        AccountGroupDeviceHistoryEntity historyEntity = new AccountGroupDeviceHistoryEntity();
        historyEntity.setAccountId(accountId);
        historyEntity.setDeviceId(accountDeviceEntity.getDeviceId());
        historyEntity.setGroup(groupEntity);
        historyEntity.setActiveFrom(now);
        historyEntity.setCreatedOn(now);
        this.accountGroupDeviceHistoryRepository.save(historyEntity);

        return MAPPER.map(this.accountGroupDeviceRepository.save(mapping));
    }
}
