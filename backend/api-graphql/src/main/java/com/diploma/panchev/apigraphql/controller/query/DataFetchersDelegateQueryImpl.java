package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.Account;
import com.diploma.panchev.apigraphql.Tenant;
import com.diploma.panchev.apigraphql.auth.CustomJwtAuthenticationToken;
import com.diploma.panchev.apigraphql.controller.mapper.GraphqlApiMapper;
import com.diploma.panchev.apigraphql.service.AccountService;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateQuery;
import graphql.schema.DataFetchingEnvironment;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetchersDelegateQueryImpl implements DataFetchersDelegateQuery {
    private final static GraphqlApiMapper MAPPER = Mappers.getMapper(GraphqlApiMapper.class);
    private final AccountService accountService;

    public DataFetchersDelegateQueryImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public Tenant tenant(DataFetchingEnvironment dataFetchingEnvironment) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CustomJwtAuthenticationToken token) {
            Tenant tenant = new Tenant();
            tenant.setId(token.getTenant());
            tenant.setName(token.getTenant().toUpperCase());
            tenant.setLogo(token.getTenant() + ".png");
            return tenant;
        }
        return null;
    }

    @Override
    public List<Account> accounts(DataFetchingEnvironment dataFetchingEnvironment) {
        return this.accountService.getAccounts().stream()
                .map(MAPPER::map)
                .toList();
    }

    @Override
    public Account account(DataFetchingEnvironment dataFetchingEnvironment, String id) {
        return this.accountService.getAccount(id)
                .map(MAPPER::map)
                .orElseThrow(() -> new RuntimeException("Account " + id + " is not found"));
    }
}
