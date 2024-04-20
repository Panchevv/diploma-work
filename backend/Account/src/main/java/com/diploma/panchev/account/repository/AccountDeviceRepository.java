package com.diploma.panchev.account.repository;

import com.diploma.panchev.account.domain.entity.AccountDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountDeviceRepository extends JpaRepository<AccountDeviceEntity, UUID>,
        JpaSpecificationExecutor<AccountDeviceEntity> {
    Optional<AccountDeviceEntity> findByAccountIdAndDeviceId(UUID accountId, String deviceId);
}
