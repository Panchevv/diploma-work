package com.diploma.panchev.apigraphql.controller.subsription;

import com.diploma.panchev.apigraphql.Measurement;
import com.diploma.panchev.apigraphql.Notification;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateSubscription;
import graphql.schema.DataFetchingEnvironment;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataFetchersDelegateSubscriptionImpl implements DataFetchersDelegateSubscription {
    @Override
    public Publisher<List<Measurement>> measurements(DataFetchingEnvironment dataFetchingEnvironment, String token) {
        return null;
    }

    @Override
    public Publisher<List<Notification>> notifications(DataFetchingEnvironment dataFetchingEnvironment, String token) {
        return null;
    }
}
