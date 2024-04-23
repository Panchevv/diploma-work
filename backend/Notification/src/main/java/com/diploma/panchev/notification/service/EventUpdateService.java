package com.diploma.panchev.notification.service;

import com.diploma.panchev.notification.domain.event.Event;
import reactor.core.publisher.Flux;

public interface EventUpdateService {
    Flux<Event> getEventUpdate();
}
