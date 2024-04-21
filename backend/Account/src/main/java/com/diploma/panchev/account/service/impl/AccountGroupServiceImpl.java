package com.diploma.panchev.account.service.impl;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.AccountGroup;
import com.diploma.panchev.account.domain.AccountGroupNeedle;
import com.diploma.panchev.account.domain.entity.AccountEntity;
import com.diploma.panchev.account.domain.entity.AccountEntity_;
import com.diploma.panchev.account.domain.entity.AccountGroupEntity;
import com.diploma.panchev.account.domain.entity.AccountGroupEntity_;
import com.diploma.panchev.account.exception.DataNotFoundException;
import com.diploma.panchev.account.mapper.AccountGroupMapper;
import com.diploma.panchev.account.repository.AccountGroupRepository;
import com.diploma.panchev.account.repository.AccountRepository;
import com.diploma.panchev.account.service.AccountGroupDeviceService;
import com.diploma.panchev.account.service.AccountGroupService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.Setter;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Setter
public class AccountGroupServiceImpl implements AccountGroupService {
    private final static AccountGroupMapper MAPPER = Mappers.getMapper(AccountGroupMapper.class);

    private final AccountGroupDeviceService accountGroupDeviceService;

    private final AccountGroupRepository accountGroupRepository;
    private final AccountRepository accountRepository;

    public AccountGroupServiceImpl(AccountGroupDeviceService accountGroupDeviceService, AccountGroupRepository accountGroupRepository, AccountRepository accountRepository) {
        this.accountGroupDeviceService = accountGroupDeviceService;
        this.accountGroupRepository = accountGroupRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public AccountGroup createAccountGroup(Account account, String name) {
        AccountEntity accountEntity = this.accountRepository.findById(account.getAccountId()).orElseThrow();
        AccountGroupEntity group = new AccountGroupEntity();
        group.setName(name);
        group.setAccount(accountEntity);
        group.setModifiedOn(OffsetDateTime.now());
        group.setCreatedOn(OffsetDateTime.now());
        return MAPPER.map(accountGroupRepository.save(group));
    }

    @Override
    public List<AccountGroup> getAccountGroups(@NonNull AccountGroupNeedle needle) {
        return this.accountGroupRepository.findAll((root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();
                    if (needle.getAccountId() != null) {
                        predicates.add(
                                criteriaBuilder.equal(root.get(AccountGroupEntity_.account).get(AccountEntity_.id), needle.getAccountId())
                        );
                    }
                    if (needle.getDeviceGroupId() != null) {
                        predicates.add(
                                criteriaBuilder.equal(root.get(AccountGroupEntity_.id), needle.getDeviceGroupId())
                        );
                    }
                    if (needle.getCeased() != null) {
                        predicates.add(
                                needle.getCeased() ?
                                        criteriaBuilder.isNotNull(root.get(AccountGroupEntity_.ceasedOn)) :
                                        criteriaBuilder.isNull(root.get(AccountGroupEntity_.ceasedOn))
                        );
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                })
                .stream()
                .map(MAPPER::map)
                .collect(Collectors.toList());
    }

    @Override
    public AccountGroup updateAccountGroup(UUID groupId, String name) {
        AccountGroupEntity current = accountGroupRepository.findById(groupId)
                .orElseThrow(() -> new DataNotFoundException("Account group is not found"));
        current.setName(name);
        current.setModifiedOn(OffsetDateTime.now());
        return MAPPER.map(accountGroupRepository.save(current));
    }

    @Override
    public Optional<AccountGroup> getAccountGroup(Account account, UUID groupId) {
        return this.accountGroupRepository.findById(groupId)
                .filter(group -> group.getAccount().getId().equals(account.getAccountId()))
                .map(MAPPER::map);
    }

    @Override
    public AccountGroup removeAccountGroup(Account account, AccountGroup accountGroup) {
        return accountGroupRepository.findById(accountGroup.getGroupId())
                .filter(group -> group.getAccount().getId().equals(account.getAccountId()))
                .map(accountGroupEntity -> {
                    accountGroupDeviceService.getAccountGroupDevices(account, accountGroup)
                            .forEach(device -> accountGroupDeviceService.removeAccountGroupDevice(account, accountGroup, device));
                    accountGroupEntity.setCeasedOn(OffsetDateTime.now());
                    accountGroupEntity.setModifiedOn(OffsetDateTime.now());
                    return MAPPER.map(accountGroupRepository.save(accountGroupEntity));
                })
                .orElseThrow(() -> new DataNotFoundException("Account group is not found"));
    }
}
