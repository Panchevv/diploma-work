package com.diploma.panchev.apigraphql.controller.mapper;

import com.diploma.panchev.apigraphql.domain.Account;
import com.diploma.panchev.apigraphql.domain.NrfAccountSettings;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper( builder = @Builder( disableBuilder = true ) )
public interface GraphqlApiMapper {
    com.diploma.panchev.apigraphql.Account map(Account account);

    com.diploma.panchev.apigraphql.NrfAccountSettings map(NrfAccountSettings nrfAccountSettings);
}
