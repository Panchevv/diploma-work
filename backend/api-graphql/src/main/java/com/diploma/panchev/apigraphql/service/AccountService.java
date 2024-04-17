package com.diploma.panchev.apigraphql.service;

import com.diploma.panchev.apigraphql.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account createAccount(String name);

    List<Account> getAccounts();

    Optional<Account> getAccount(String accountId);
}
