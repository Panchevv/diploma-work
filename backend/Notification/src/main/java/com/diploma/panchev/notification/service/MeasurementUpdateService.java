package com.diploma.panchev.notification.service;

import com.diploma.panchev.notification.domain.measurement.MeasurementUpdate;
import reactor.core.publisher.Flux;

public interface MeasurementUpdateService {
    Flux<MeasurementUpdate> getMeasurementUpdate();
}
