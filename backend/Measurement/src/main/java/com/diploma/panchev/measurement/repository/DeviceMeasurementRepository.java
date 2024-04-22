package com.diploma.panchev.measurement.repository;

import com.diploma.panchev.measurement.domain.MeasurementType;
import com.diploma.panchev.measurement.domain.entity.DeviceMeasurementEntity;
import com.diploma.panchev.measurement.domain.entity.DeviceMeasurementEntity_;
import com.diploma.panchev.measurement.utils.SpecificationUtils;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
public interface DeviceMeasurementRepository extends CrudRepository<DeviceMeasurementEntity, Long>, JpaSpecificationExecutor<DeviceMeasurementEntity> {
    Optional<DeviceMeasurementEntity> findFirstByDeviceIdAndTypeOrderByCreatedAtDesc(String device, MeasurementType type);

    default Page<DeviceMeasurementEntity> findDeviceHistory(@NonNull String deviceId, MeasurementType type,
                                                            OffsetDateTime from, OffsetDateTime to, Long fromId,
                                                            Pageable pageable) {
        Specification<DeviceMeasurementEntity> specification = SpecificationUtils.getHistorySpecification(deviceId, type, from, to);
        if (fromId != null) {
            specification = specification.and((r, q, cb) -> cb.greaterThan(r.get(DeviceMeasurementEntity_.id), fromId));
        }
        return this.findAll(specification, pageable);
    }

    default long countDeviceHistory(@NonNull String deviceId, MeasurementType type,
                                    OffsetDateTime from, OffsetDateTime to) {
        return this.count(SpecificationUtils.getHistorySpecification(deviceId, type, from, to));
    }
}
