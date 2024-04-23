package com.diploma.panchev.notification.utils;

import com.diploma.panchev.notification.domain.event.entity.EventEntity;
import com.diploma.panchev.notification.domain.event.entity.EventEntity_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public final class SpecificationsUtils {
    public static Specification<EventEntity> fromDeviceNeedle(String groupId, String deviceId, OffsetDateTime lastTime) {
        return (root, query, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            predicateList.add(cb.equal(root.get(EventEntity_.groupId), groupId));
            if(!deviceId.isBlank()) {
                predicateList.add(cb.equal(root.get(EventEntity_.deviceId), deviceId));
            }
            if(lastTime != null) {
                predicateList.add(cb.lessThan(root.get(EventEntity_.createdAt), lastTime));
            }
            query.orderBy(cb.asc(root.get(EventEntity_.createdAt)));
            return cb.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
