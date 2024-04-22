package com.diploma.panchev.measurement.utils;

import com.diploma.panchev.measurement.domain.MeasurementType;
import com.diploma.panchev.measurement.domain.entity.DeviceMeasurementEntity;
import com.diploma.panchev.measurement.domain.entity.DeviceMeasurementEntity_;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;

public final class SpecificationUtils {
    private SpecificationUtils() {}

    public static Specification<DeviceMeasurementEntity> getHistorySpecification(@NonNull String deviceId, MeasurementType type,
                                                                                 OffsetDateTime from, OffsetDateTime to) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();
            predicates.add(criteriaBuilder.equal(root.get(DeviceMeasurementEntity_.deviceId), deviceId));
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get(DeviceMeasurementEntity_.type), type));
            }
            if (from != null && to != null) {
                predicates.add(criteriaBuilder.between(root.get(DeviceMeasurementEntity_.createdAt), from, to));
            } else {
                if (from != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(DeviceMeasurementEntity_.createdAt), from));
                }
                if (to != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(DeviceMeasurementEntity_.createdAt), to));
                }
            }
            query.orderBy(criteriaBuilder.asc(root.get(DeviceMeasurementEntity_.createdAt)));
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }
}
