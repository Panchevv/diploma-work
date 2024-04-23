package com.diploma.panchev.notification.repository;

import com.diploma.panchev.notification.domain.event.entity.EventEntity;
import com.diploma.panchev.notification.utils.SpecificationsUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, UUID>, JpaSpecificationExecutor<EventEntity> {
    default Page<EventEntity> getEventHistory(String groupId, String deviceId, String lastId, int pageSize) {
        OffsetDateTime lastTime = null;
        if(!lastId.isBlank()) {
            lastTime = this.findById(UUID.fromString(lastId))
                    .map(EventEntity::getCreatedAt)
                    .orElse(null);
        }
        Specification<EventEntity> specification = SpecificationsUtils.fromDeviceNeedle(groupId, deviceId, lastTime);
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Order.desc("createdAt")));
        return this.findAll(specification, pageable);
    }
}
