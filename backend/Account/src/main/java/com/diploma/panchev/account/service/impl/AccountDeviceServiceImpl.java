package com.diploma.panchev.account.service.impl;

import com.diploma.panchev.account.domain.*;
import com.diploma.panchev.account.domain.entity.*;
import com.diploma.panchev.account.mapper.AccountDeviceMapper;
import com.diploma.panchev.account.repository.AccountDeviceHistoryRepository;
import com.diploma.panchev.account.repository.AccountDeviceRepository;
import com.diploma.panchev.account.repository.AccountRepository;
import com.diploma.panchev.account.service.AccountDeviceService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Service
@Transactional
public class AccountDeviceServiceImpl implements AccountDeviceService {
    private final static AccountDeviceMapper MAPPER = Mappers.getMapper(AccountDeviceMapper.class);

    private final AccountDeviceRepository accountDeviceRepository;
    private final AccountRepository accountRepository;
    private final AccountDeviceHistoryRepository accountDeviceHistoryRepository;

    public AccountDeviceServiceImpl(AccountDeviceRepository accountDeviceRepository, AccountRepository accountRepository, AccountDeviceHistoryRepository accountDeviceHistoryRepository) {
        this.accountDeviceRepository = accountDeviceRepository;
        this.accountRepository = accountRepository;
        this.accountDeviceHistoryRepository = accountDeviceHistoryRepository;
    }

    @Override
    public Optional<AccountDevice> getAccountDevice(UUID accountId, String deviceId) {
        return this.accountDeviceRepository.findByAccountIdAndDeviceId(accountId, deviceId)
                .map(MAPPER::map);
    }

    @Override
    @Transactional
    public AccountDevice addAccountDevice(Account account, String deviceId, String name) {
        log.trace("addAccountDevice: account={} deviceId={}", account, deviceId);
        OffsetDateTime now = OffsetDateTime.now();
        AccountEntity accountEntity = this.accountRepository.findById(account.getAccountId()).orElseThrow();
        AccountDeviceEntity accountDeviceEntity = this.accountDeviceRepository.findByDeviceId(deviceId)
                .orElseGet(() -> {
                    AccountDeviceEntity entity = new AccountDeviceEntity();
                    entity.setName(Objects.requireNonNullElseGet(name, () -> "Device " + deviceId));
                    entity.setDeviceId(deviceId);
                    return entity;
                });

        if (accountDeviceEntity.getAccount() != null
                && accountEntity.getId().equals(accountDeviceEntity.getAccount().getId())) {
            log.trace("addAccountDevice: found existing mapping");
            return MAPPER.map(accountDeviceEntity);
        }

        Optional<AccountDeviceHistoryEntity> optDevice =
                this.accountDeviceHistoryRepository.findActiveByDeviceId(deviceId);

        if (optDevice.isPresent()) {
            log.trace("addAccountDevice: found existing mapping");
            AccountDeviceHistoryEntity current = optDevice.get();
            current.setActiveTo(now);
            current.setModifiedOn(now);
            this.accountDeviceHistoryRepository.save(current);
        }

        AccountDeviceHistoryEntity historyEntity = new AccountDeviceHistoryEntity();
        historyEntity.setAccountId(accountEntity.getId());
        historyEntity.setDeviceId(deviceId);
        historyEntity.setName(accountEntity.getName());
        historyEntity.setActiveFrom(now.plusNanos(1));
        historyEntity.setCreatedOn(now);
        this.accountDeviceHistoryRepository.save(historyEntity);

        accountDeviceEntity.setAccount(accountEntity);
        if (accountDeviceEntity.getCreatedAt() == null) {
            accountDeviceEntity.setCreatedAt(OffsetDateTime.now());
        } else {
            accountDeviceEntity.setModifiedAt(OffsetDateTime.now());
        }

        return MAPPER.map(accountDeviceRepository.save(accountDeviceEntity));
    }

    @Override
    public AccountDevice updateAccountDevice(UUID accountId, String deviceId, String name) {
        log.trace("updateAccountDevice: accountId={} deviceId={} name={}", accountId, deviceId, name);
        AccountDeviceEntity accountDeviceEntity = this.accountDeviceRepository.findByAccountIdAndDeviceId(accountId, deviceId)
                .orElseThrow();

        AccountDeviceHistoryEntity currentHistory = accountDeviceHistoryRepository.findActiveByDeviceId(deviceId)
                .orElseThrow();
        OffsetDateTime now = OffsetDateTime.now();

        currentHistory.setActiveTo(now);
        currentHistory.setModifiedOn(now);
        this.accountDeviceHistoryRepository.save(currentHistory);

        accountDeviceEntity.setName(name);
        this.accountDeviceRepository.save(accountDeviceEntity);

        AccountDeviceHistoryEntity historyEntity = new AccountDeviceHistoryEntity();
        historyEntity.setAccountId(accountDeviceEntity.getAccount().getId());
        historyEntity.setDeviceId(accountDeviceEntity.getDeviceId());
        historyEntity.setName(accountDeviceEntity.getName());
        historyEntity.setActiveFrom(now.plusNanos(1));
        historyEntity.setCreatedOn(now);
        this.accountDeviceHistoryRepository.save(historyEntity);

        return MAPPER.map(accountDeviceEntity);
    }

    @Override
    public Relay<AccountDevice> getDevices(AccountDeviceNeedle needle, String fromDeviceId, int size) {
        Page<AccountDevice> page =
                this.accountDeviceRepository.findAll(
                                (root, query, criteriaBuilder) -> {
                                    List<Predicate> predicates = new ArrayList<>();
                                    if (needle.accountId() != null) {
                                        predicates.add(
                                                criteriaBuilder.equal(
                                                        root.get(AccountDeviceEntity_.account).get(AccountEntity_.id),
                                                        needle.accountId()
                                                )
                                        );
                                    }
                                    if (needle.ungrouped() != null) {
                                        Join<AccountDeviceEntity, AccountGroupDeviceEntity> join =
                                                root.join(AccountDeviceEntity_.group, JoinType.LEFT);
                                        predicates.add(
                                                needle.ungrouped() ?
                                                        criteriaBuilder.isNull(join.get(AccountGroupDeviceEntity_.id)) :
                                                        criteriaBuilder.isNotNull(join.get(AccountGroupDeviceEntity_.id))
                                        );
                                    }
                                    if (fromDeviceId != null) {
                                        predicates.add(
                                                criteriaBuilder.greaterThan(
                                                        root.get(AccountDeviceEntity_.deviceId),
                                                        fromDeviceId
                                                )
                                        );
                                    }
                                    query.orderBy(criteriaBuilder.asc(root.get(AccountDeviceEntity_.deviceId)));
                                    return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
                                },
                                PageRequest.of(0, size)
                        )
                        .map(MAPPER::map);

        long totalElements = page.getTotalElements();

        Relay<AccountDevice> relay = new Relay<>();
        relay.setItems(page.getContent());
        RelayPageInfo pageInfo = new RelayPageInfo();
        relay.setPageInfo(pageInfo);
        pageInfo.setCount(totalElements);
        pageInfo.setHasNextPage(page.getNumberOfElements() == size);
        pageInfo.setHasPreviousPage(totalElements < pageInfo.getCount());

        return relay;
    }

    @Override
    public AccountDevice removeAccountDevice(Account account, String deviceId) {
        AccountDeviceEntity accountDeviceEntity = this.accountDeviceRepository.findByAccountIdAndDeviceId(account.getAccountId(), deviceId)
                .orElseThrow();
        OffsetDateTime now = OffsetDateTime.now();

        AccountDeviceHistoryEntity current = accountDeviceHistoryRepository.findActiveByDeviceId(deviceId)
                .orElseThrow();
        current.setActiveTo(now);
        current.setModifiedOn(now);
        this.accountDeviceHistoryRepository.save(current);
        this.accountDeviceRepository.delete(accountDeviceEntity);
        return MAPPER.map(accountDeviceEntity);
    }
}
