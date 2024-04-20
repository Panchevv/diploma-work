package com.diploma.panchev.account.mapper.database;

import com.diploma.panchev.account.domain.AccountGroup;
import com.diploma.panchev.account.domain.entity.AccountGroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountGroupMapper {
    @Mapping(source = "id", target = "groupId")
    AccountGroup map(AccountGroupEntity entity);
}
