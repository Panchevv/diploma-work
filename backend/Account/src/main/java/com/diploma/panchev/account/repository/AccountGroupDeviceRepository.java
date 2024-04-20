package com.diploma.panchev.account.repository;

import com.diploma.panchev.account.domain.entity.AccountGroupDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountGroupDeviceRepository extends JpaRepository<AccountGroupDeviceEntity, UUID>, JpaSpecificationExecutor<AccountGroupDeviceEntity> {
    Optional<AccountGroupDeviceEntity> findByAccountDeviceId(UUID mappingId);
}
