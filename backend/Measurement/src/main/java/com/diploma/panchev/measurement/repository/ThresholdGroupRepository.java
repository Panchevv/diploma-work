package com.diploma.panchev.measurement.repository;

import com.diploma.panchev.measurement.domain.entity.ThresholdGroupEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ThresholdGroupRepository extends CrudRepository<ThresholdGroupEntity, UUID> {
    List<ThresholdGroupEntity> findAllByThresholdId(UUID thresholdId);

    List<ThresholdGroupEntity> findAllByGroupId(String groupId);
}
