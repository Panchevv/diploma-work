package com.diploma.panchev.account.service.impl;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.AccountNeedle;
import com.diploma.panchev.account.domain.entity.*;
import com.diploma.panchev.account.mapper.database.AccountMapper;
import com.diploma.panchev.account.repository.AccountRepository;
import com.diploma.panchev.account.repository.AccountUserRepository;
import com.diploma.panchev.account.service.AccountService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.Setter;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@Setter
public class AccountServiceImpl implements AccountService {
    private final static AccountMapper MAPPER = Mappers.getMapper(AccountMapper.class);

    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    public AccountServiceImpl(AccountRepository accountRepository, AccountUserRepository accountUserRepository) {
        this.accountRepository = accountRepository;
        this.accountUserRepository = accountUserRepository;
    }

    @Override
    @Transactional
    public Account createAccount(String name, String userId) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setName(name);
        accountEntity.setModifiedOn(OffsetDateTime.now());
        accountEntity.setCreatedOn(OffsetDateTime.now());
        accountEntity = accountRepository.save(accountEntity);

        AccountUserEntity userEntity = new AccountUserEntity();
        AccountUserId id = new AccountUserId();
        id.setAccountId(accountEntity.getId());
        id.setUserId(userId);
        userEntity.setId(id);
        userEntity.setAccount(accountEntity);
        userEntity.setCreatedAt(OffsetDateTime.now());
        this.accountUserRepository.save(userEntity);
        return MAPPER.map(accountEntity);
    }

    @Override
    public Page<Account> getAccounts(AccountNeedle needle) {
        return this.getAccounts(needle, null, null);
    }

    private Page<Account> getAccounts(@NonNull AccountNeedle needle, Integer page, Integer size) {
        Page<AccountEntity> accountPage = accountRepository.findAll(
                (root, query, builder) -> {
                    List<Predicate> p = new LinkedList<>();
                    if (needle.getAccountId() != null) {
                        p.add(builder.equal(root.get(AccountEntity_.id), needle.getAccountId()));
                    }
                    if (needle.getUserId() != null) {
                        Join<AccountEntity, AccountUserEntity> joinRoot = root.join(AccountEntity_.users);
                        p.add(builder.equal(
                                        joinRoot.get(AccountUserEntity_.id).get(AccountUserId_.userId),
                                        needle.getUserId()
                                )
                        );
                    }
                    if (!needle.isInactive()) {
                        p.add(builder.isNull(root.get(AccountEntity_.ceasedOn)));
                    }
                    return builder.and(p.toArray(new Predicate[0]));
                },
                size == null ? Pageable.unpaged() : PageRequest.of(page == null ? 0 : page, size)
        );
        return accountPage.map(MAPPER::map);
    }
}
