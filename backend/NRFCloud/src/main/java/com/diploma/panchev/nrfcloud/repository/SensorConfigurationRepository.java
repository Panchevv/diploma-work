package com.diploma.panchev.nrfcloud.repository;

import com.diploma.panchev.nrfcloud.domain.entity.SensorConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorConfigurationRepository extends JpaRepository<SensorConfiguration, String> {
    void deleteByDeviceId(String deviceId);

    void deleteAllByGroupId(String groupId);
}
