package com.diploma.panchev.apigraphql.controller.mapper;

import com.diploma.panchev.apigraphql.domain.Account;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper( builder = @Builder( disableBuilder = true ) )
public interface GraphqlApiMapper {
    com.diploma.panchev.apigraphql.Account map(Account account);
}
