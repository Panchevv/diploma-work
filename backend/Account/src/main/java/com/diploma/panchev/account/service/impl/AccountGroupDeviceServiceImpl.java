package com.diploma.panchev.account.service.impl;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.AccountGroup;
import com.diploma.panchev.account.domain.AccountGroupDevice;
import com.diploma.panchev.account.domain.AccountGroupDeviceNeedle;
import com.diploma.panchev.account.domain.entity.*;
import com.diploma.panchev.account.exception.DataNotFoundException;
import com.diploma.panchev.account.mapper.AccountGroupDeviceMapper;
import com.diploma.panchev.account.repository.AccountDeviceRepository;
import com.diploma.panchev.account.repository.AccountGroupDeviceHistoryRepository;
import com.diploma.panchev.account.repository.AccountGroupDeviceRepository;
import com.diploma.panchev.account.repository.AccountGroupRepository;
import com.diploma.panchev.account.service.AccountGroupDeviceService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public List<AccountGroupDevice> getAccountGroupDevices(AccountGroupDeviceNeedle needle) {
        return this.accountGroupDeviceRepository.findAll(
                        (root, query, criteriaBuilder) -> {
                            List<Predicate> predicates = new ArrayList<>();
                            if (needle.getAccountId() != null) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(AccountGroupDeviceEntity_.group).get(AccountGroupEntity_.account).get(AccountEntity_.id),
                                                needle.getAccountId()
                                        )
                                );
                            }
                            if (needle.getDeviceGroupId() != null) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(AccountGroupDeviceEntity_.group).get(AccountGroupEntity_.id), needle.getDeviceGroupId()
                                        )
                                );
                            }
                            if (needle.getDeviceId() != null) {
                                predicates.add(
                                        criteriaBuilder.equal(
                                                root.get(AccountGroupDeviceEntity_.accountDevice).get(AccountDeviceEntity_.deviceId), needle.getDeviceId()
                                        )
                                );
                            }
                            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
                        }
                )
                .stream()
                .map(MAPPER::map)
                .toList();
    }

    @Override
    public AccountGroupDevice removeAccountGroupDevice(@NonNull Account account, @NonNull AccountGroup accountGroup, @NonNull AccountGroupDevice device) {
        AccountGroupDeviceEntity mapping =
                this.accountGroupDeviceRepository.findByAccountDeviceAccountIdAndGroupIdAndAccountDeviceDeviceId(account.getAccountId(), accountGroup.getGroupId(), device.getDeviceId())
                        .orElseThrow(() -> new DataNotFoundException("Device is not found"));

        AccountGroupDeviceHistoryEntity entity = this.accountGroupDeviceHistoryRepository.findActiveByDeviceId(device.getDeviceId())
                .orElseThrow(() -> new DataNotFoundException("Device history is not found"));

        entity.setActiveTo(OffsetDateTime.now());
        entity.setModifiedOn(OffsetDateTime.now());
        this.accountGroupDeviceHistoryRepository.save(entity);
        this.accountGroupDeviceRepository.delete(mapping);
        return MAPPER.map(mapping);
    }

    @Override
    public Collection<AccountGroupDevice> getAccountGroupDevices(Account account, AccountGroup accountGroup) {
        return accountGroupDeviceRepository.findByGroupId(accountGroup.getGroupId())
                .stream()
                .map(MAPPER::map)
                .collect(Collectors.toList());
    }
}
