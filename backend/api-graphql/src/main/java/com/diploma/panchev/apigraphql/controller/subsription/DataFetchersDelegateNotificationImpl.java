package com.diploma.panchev.apigraphql.controller.subsription;

import com.diploma.panchev.apigraphql.Notification;
import com.diploma.panchev.apigraphql.NotificationThreshold;
import com.diploma.panchev.apigraphql.util.DataFetchersDelegateNotification;
import graphql.schema.DataFetchingEnvironment;
import org.dataloader.DataLoader;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class DataFetchersDelegateNotificationImpl implements DataFetchersDelegateNotification {
    @Override
    public CompletableFuture<NotificationThreshold> threshold(DataFetchingEnvironment dataFetchingEnvironment, DataLoader<String, NotificationThreshold> dataLoader, Notification origin) {
        return CompletableFuture.supplyAsync(origin::getThreshold);
    }

    @Override
    public NotificationThreshold threshold(DataFetchingEnvironment dataFetchingEnvironment, Notification origin) {
        return origin.getThreshold();
    }
}
