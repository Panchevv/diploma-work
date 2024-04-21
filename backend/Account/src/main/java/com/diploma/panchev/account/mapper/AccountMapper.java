package com.diploma.panchev.account.mapper;

import com.diploma.panchev.account.domain.Account;
import com.diploma.panchev.account.domain.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountMapper {
    @Mapping(source = "id", target = "accountId")
    Account map(AccountEntity entity);
}
