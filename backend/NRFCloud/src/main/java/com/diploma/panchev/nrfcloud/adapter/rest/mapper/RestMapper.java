package com.diploma.panchev.nrfcloud.adapter.rest.mapper;

import com.diploma.panchev.nrfcloud.domain.AccountDevice;
import com.diploma.panchev.nrfcloud.domain.AccountInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RestMapper {
    @Mapping(source = "team.tenantId", target = "tenantId")
    AccountInfo map(com.diploma.panchev.nrfcloud.adapter.rest.model.AccountInfo accountInfo);

    AccountDevice map(com.diploma.panchev.nrfcloud.adapter.rest.model.AccountDevice accountInfo);
}
