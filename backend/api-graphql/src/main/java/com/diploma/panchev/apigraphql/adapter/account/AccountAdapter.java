package com.diploma.panchev.apigraphql.adapter.account;

import com.diploma.panchev.apigraphql.domain.Account;

public interface AccountAdapter {
    Account createAccount(String name, String userId);
}
