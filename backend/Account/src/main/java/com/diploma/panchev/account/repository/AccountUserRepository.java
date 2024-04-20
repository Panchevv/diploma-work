package com.diploma.panchev.account.repository;

import com.diploma.panchev.account.domain.entity.AccountUserEntity;
import com.diploma.panchev.account.domain.entity.AccountUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountUserRepository extends JpaRepository<AccountUserEntity, AccountUserId> {
}
