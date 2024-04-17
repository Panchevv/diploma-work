package com.diploma.panchev.apigraphql.adapter.account;

import com.diploma.panchev.apigraphql.domain.Account;

import java.util.List;

public interface AccountAdapter {
    Account createAccount(String name, String userId);

    List<Account> getAccounts(String userId);
}
