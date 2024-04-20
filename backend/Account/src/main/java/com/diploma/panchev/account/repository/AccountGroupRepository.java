package com.diploma.panchev.account.repository;

import com.diploma.panchev.account.domain.entity.AccountGroupEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountGroupRepository extends CrudRepository<AccountGroupEntity, UUID>, JpaSpecificationExecutor<AccountGroupEntity> {
}
