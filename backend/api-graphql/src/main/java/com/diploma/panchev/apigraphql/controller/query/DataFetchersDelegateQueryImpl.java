package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.Account;
import com.diploma.panchev.apigraphql.Tenant;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateQuery;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetchersDelegateQueryImpl implements DataFetchersDelegateQuery {
    @Override
    public Tenant tenant(DataFetchingEnvironment dataFetchingEnvironment) {
        return null;
    }

    @Override
    public List<Account> accounts(DataFetchingEnvironment dataFetchingEnvironment) {
        return List.of();
    }

    @Override
    public Account account(DataFetchingEnvironment dataFetchingEnvironment, String id) {
        return null;
    }
}
