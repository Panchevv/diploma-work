package com.diploma.panchev.nrfcloud.repository;

import com.diploma.panchev.nrfcloud.domain.entity.ApiConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiConfigurationRepository extends JpaRepository<ApiConfiguration, String> {
}
