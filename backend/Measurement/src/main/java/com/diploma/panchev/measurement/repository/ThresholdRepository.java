package com.diploma.panchev.measurement.repository;

import com.diploma.panchev.measurement.domain.entity.ThresholdEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ThresholdRepository extends CrudRepository<ThresholdEntity, UUID>, JpaSpecificationExecutor<ThresholdEntity> {
    List<ThresholdEntity> findAllByAccountId(String accountId);
}
