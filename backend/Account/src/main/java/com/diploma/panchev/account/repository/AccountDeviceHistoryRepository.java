package com.diploma.panchev.account.repository;

import com.diploma.panchev.account.domain.entity.AccountDeviceHistoryEntity;
import com.diploma.panchev.account.domain.entity.AccountDeviceHistoryEntity_;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface AccountDeviceHistoryRepository extends CrudRepository<AccountDeviceHistoryEntity, Long>, JpaSpecificationExecutor<AccountDeviceHistoryEntity> {
    default Optional<AccountDeviceHistoryEntity> findActiveByDeviceId(final String deviceId) {
        OffsetDateTime now = OffsetDateTime.now();
        return findOne(
                (root, query, builder) ->
                        builder.and(
                                builder.equal(root.get(AccountDeviceHistoryEntity_.deviceId), deviceId),
                                builder.lessThanOrEqualTo(root.get(AccountDeviceHistoryEntity_.activeFrom), now),
                                builder.or(
                                        builder.isNull(root.get(AccountDeviceHistoryEntity_.activeTo)),
                                        builder.greaterThanOrEqualTo(root.get(AccountDeviceHistoryEntity_.activeTo), now)
                                )
                        )
        );
    }
}
