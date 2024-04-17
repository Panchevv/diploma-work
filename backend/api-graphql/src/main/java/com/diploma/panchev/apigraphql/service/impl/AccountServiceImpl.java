package com.diploma.panchev.apigraphql.service.impl;

import com.diploma.panchev.apigraphql.adapter.account.AccountAdapter;
import com.diploma.panchev.apigraphql.domain.Account;
import com.diploma.panchev.apigraphql.service.AccountService;
import com.diploma.panchev.apigraphql.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountAdapter accountAdapter;
    private final UserService userService;

    public AccountServiceImpl(AccountAdapter accountAdapter, UserService userService) {
        this.accountAdapter = accountAdapter;
        this.userService = userService;
    }

    @Override
    public Account createAccount(String name) {
        return this.accountAdapter.createAccount(
                name,
                userService.getUserId()
        );
    }
}
