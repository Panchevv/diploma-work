package com.diploma.panchev.account.service;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.AccountNeedle;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {
    Account createAccount(String name, String userId);

    Page<Account> getAccounts(AccountNeedle needle);

    Optional<Account> getAccount(UUID accountId);
}
