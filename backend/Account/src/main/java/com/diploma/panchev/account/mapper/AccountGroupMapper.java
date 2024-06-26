package com.diploma.panchev.account.mapper;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.AccountGroup;
import com.diploma.panchev.account.domain.entity.AccountEntity;
import com.diploma.panchev.account.domain.entity.AccountGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountGroupMapper {
    @Mapping(source = "id", target = "groupId")
    AccountGroup map(AccountGroupEntity entity);

    @Mapping(source = "id", target = "accountId")
    Account map(AccountEntity accountEntity);
}
