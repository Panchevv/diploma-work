package com.diploma.panchev.account.repository;

import com.diploma.panchev.account.domain.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends CrudRepository<AccountEntity, UUID>, JpaSpecificationExecutor<AccountEntity> {
}
