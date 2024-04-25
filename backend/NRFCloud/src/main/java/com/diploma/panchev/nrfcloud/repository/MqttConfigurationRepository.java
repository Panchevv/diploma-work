package com.diploma.panchev.nrfcloud.repository;

import com.diploma.panchev.nrfcloud.domain.entity.MqttConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MqttConfigurationRepository extends JpaRepository<MqttConfiguration, String> {
}
