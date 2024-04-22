package com.diploma.panchev.measurement.repository;

import com.diploma.panchev.measurement.domain.entity.ThresholdHistoryEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThresholdHistoryRepository extends CrudRepository<ThresholdHistoryEntity, Long>, JpaSpecificationExecutor<ThresholdHistoryEntity> {
}
