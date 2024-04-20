package com.diploma.panchev.account.mapper.database;

import com.diploma.panchev.account.domain.AccountGroupDevice;
import com.diploma.panchev.account.domain.entity.AccountGroupDeviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AccountGroupDeviceMapper {
    @Mapping(source = "accountDevice.deviceId", target = "deviceId")
    @Mapping(source = "accountDevice.name", target = "name")
    @Mapping(source = "accountDevice.account.id", target = "accountId")
    @Mapping(source = "accountDevice.account", target = "account")
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "createdAt", target = "createdOn")
    AccountGroupDevice map(AccountGroupDeviceEntity entity);
}
