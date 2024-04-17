package com.diploma.panchev.apigraphql.controller.query;

import com.diploma.panchev.apigraphql.Tenant;
import com.diploma.panchev.apigraphql.TenantOwner;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateTenant;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class DataFetchersDelegateTenantImpl implements DataFetchersDelegateTenant {
    @Override
    public CompletableFuture<TenantOwner> owner(DataFetchingEnvironment dataFetchingEnvironment, DataLoader<String, TenantOwner> dataLoader, Tenant origin) {
        return null;
    }

    @Override
    public TenantOwner owner(DataFetchingEnvironment dataFetchingEnvironment, Tenant origin) {
        return null;
    }
}
