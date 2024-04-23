package com.diploma.panchev.notification.repository;

import com.diploma.panchev.notification.domain.event.entity.ThresholdEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MeasurementThresholdRepository extends JpaRepository<ThresholdEntity, UUID> {
}
