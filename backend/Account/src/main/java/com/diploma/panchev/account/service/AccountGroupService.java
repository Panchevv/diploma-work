package com.diploma.panchev.account.service;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.AccountGroup;
import com.diploma.panchev.account.domain.AccountGroupNeedle;

import java.util.List;
import java.util.UUID;

public interface AccountGroupService {
    AccountGroup createAccountGroup(Account account, String name);

    List<AccountGroup> getAccountGroups(AccountGroupNeedle needle);

    AccountGroup updateAccountGroup(UUID groupId, String name);
}
