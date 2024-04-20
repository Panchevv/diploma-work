package com.diploma.panchev.account.mapper.database;

import com.diploma.panchev.account.domain.AccountDevice;
import com.diploma.panchev.account.domain.entity.AccountDeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountDeviceMapper {
    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "createdAt", target = "createdOn")
    @Mapping(source = "group.group", target = "group")
    @Mapping(source = "group.group.id", target = "groupId")
    @Mapping(source = "id", target = "accountDeviceId")
    AccountDevice map(AccountDeviceEntity entity);
}
