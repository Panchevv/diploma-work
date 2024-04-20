package com.diploma.panchev.account.repository;

import com.diploma.panchev.account.domain.entity.AccountGroupDeviceHistoryEntity;
import com.diploma.panchev.account.domain.entity.AccountGroupDeviceHistoryEntity_;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface AccountGroupDeviceHistoryRepository extends CrudRepository<AccountGroupDeviceHistoryEntity, Long>, JpaSpecificationExecutor<AccountGroupDeviceHistoryEntity> {
    default Optional<AccountGroupDeviceHistoryEntity> findActiveByDeviceId(String deviceId) {
        final OffsetDateTime now = OffsetDateTime.now();
        return findOne(
                (root, query, builder) ->
                        builder.and(
                                builder.equal(root.get(AccountGroupDeviceHistoryEntity_.deviceId), deviceId),
                                builder.lessThanOrEqualTo(root.get(AccountGroupDeviceHistoryEntity_.activeFrom), now),
                                builder.or(
                                        builder.isNull(root.get(AccountGroupDeviceHistoryEntity_.activeTo)),
                                        builder.greaterThanOrEqualTo(root.get(AccountGroupDeviceHistoryEntity_.activeTo), now)
                                )
                        )
        );
    }
}
