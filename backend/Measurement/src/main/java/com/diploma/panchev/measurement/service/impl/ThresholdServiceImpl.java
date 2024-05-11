package com.diploma.panchev.measurement.service.impl;

import com.diploma.panchev.measurement.domain.DeviceMeasurement;
import com.diploma.panchev.measurement.domain.Threshold;
import com.diploma.panchev.measurement.domain.ThresholdHistory;
import com.diploma.panchev.measurement.domain.ThresholdRequest;
import com.diploma.panchev.measurement.domain.entity.ThresholdEntity;
import com.diploma.panchev.measurement.domain.entity.ThresholdGroupEntity;
import com.diploma.panchev.measurement.domain.entity.ThresholdHistoryEntity;
import com.diploma.panchev.measurement.repository.ThresholdGroupRepository;
import com.diploma.panchev.measurement.repository.ThresholdHistoryRepository;
import com.diploma.panchev.measurement.repository.ThresholdRepository;
import com.diploma.panchev.measurement.service.ThresholdService;
import com.diploma.panchev.measurement.service.mapper.ThresholdMapper;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Setter
@Slf4j
public class ThresholdServiceImpl implements ThresholdService {
    private static final ThresholdMapper MAPPER = Mappers.getMapper(ThresholdMapper.class);

    private final ThresholdRepository repository;
    private final ThresholdGroupRepository thresholdGroupRepository;
    private final ThresholdHistoryRepository historyRepository;

    public ThresholdServiceImpl(ThresholdRepository repository, ThresholdGroupRepository thresholdGroupRepository, ThresholdHistoryRepository historyRepository) {
        this.repository = repository;
        this.thresholdGroupRepository = thresholdGroupRepository;
        this.historyRepository = historyRepository;
    }

    @Override
    public Threshold createThreshold(ThresholdRequest request) {
        ThresholdEntity entity = new ThresholdEntity();
        entity.setName(request.getName());
        entity.setOperator(request.getOperator());
        entity.setType(request.getMeasurementType());
        entity.setValue(request.getValue());
        entity.setCreatedAt(OffsetDateTime.now());
        entity.setAccountId(request.getAccountId());
        Threshold persistedThreshold = MAPPER.map(this.repository.save(entity));

        persistedThreshold.setGroupIds(this.persistThresholdGroups(request.getGroupIds(), persistedThreshold.getId()));

        return persistedThreshold;
    }

    @Override
    public Threshold editThreshold(UUID id, ThresholdRequest request) {
        ThresholdEntity entity = this.repository.findById(id).orElseThrow();
        entity.setName(request.getName());
        entity.setOperator(request.getOperator());
        entity.setType(request.getMeasurementType());
        entity.setValue(request.getValue());
        entity.setModifiedAt(OffsetDateTime.now());
        Threshold persistedThreshold = MAPPER.map(this.repository.save(entity));

        persistedThreshold.setGroupIds(this.persistThresholdGroups(request.getGroupIds(), persistedThreshold.getId()));

        return persistedThreshold;
    }

    @Override
    @Transactional
    public Threshold removeThreshold(UUID id) {
        ThresholdEntity entity = this.repository.findById(id).orElseThrow();
        this.thresholdGroupRepository.deleteAll(this.thresholdGroupRepository.findAllByThresholdId(entity.getId()));
        this.repository.delete(entity);
        return MAPPER.map(entity);
    }

    @Override
    public List<Threshold> getThresholds(String accountId) {
        return this.repository.findAllByAccountId(accountId)
                .stream()
                .map(MAPPER::map)
                .peek(threshold ->
                        threshold.setGroupIds(
                                thresholdGroupRepository.findAllByThresholdId(threshold.getId())
                                        .stream()
                                        .map(ThresholdGroupEntity::getGroupId)
                                        .toList()
                        )
                )
                .toList();
    }

    private List<String> persistThresholdGroups(List<String> groups, UUID thresholdId) {
        this.thresholdGroupRepository.deleteAll(thresholdGroupRepository.findAllByThresholdId(thresholdId));

        return groups.stream()
                .map(group -> {
                    ThresholdGroupEntity thresholdGroupEntity = new ThresholdGroupEntity();
                    thresholdGroupEntity.setThresholdId(thresholdId);
                    thresholdGroupEntity.setGroupId(group);
                    return thresholdGroupEntity;
                })
                .map(this.thresholdGroupRepository::save)
                .map(ThresholdGroupEntity::getGroupId)
                .toList();
    }

    @Override
    public List<ThresholdHistory> evaluateDeviceMeasurement(DeviceMeasurement measurement) {
        return this.thresholdGroupRepository.findAllByGroupId(measurement.getGroupId())
                .stream()
                .map(ThresholdGroupEntity::getThresholdId)
                .map(this.repository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(threshold -> threshold.evaluate(measurement.getMeasurement().getValue()))
                .map(threshold -> {
                    ThresholdHistoryEntity historyEntity = new ThresholdHistoryEntity();
                    historyEntity.setThreshold(threshold);
                    historyEntity.setDeviceId(measurement.getDeviceId());
                    if (measurement.getGroupId() != null) {
                        historyEntity.setGroupId((measurement.getGroupId()));
                    }
                    historyEntity.setOperator(threshold.getOperator());
                    historyEntity.setType(threshold.getType());
                    historyEntity.setActual(measurement.getMeasurement().getValue());
                    historyEntity.setValue(threshold.getValue());
                    historyEntity.setCreatedAt(OffsetDateTime.now());
                    ThresholdHistory thresholdHistory = MAPPER.map(measurement.getGroupId(), this.historyRepository.save(historyEntity));
                    thresholdHistory.getThreshold().setGroupIds(
                            thresholdGroupRepository.findAllByThresholdId(threshold.getId())
                                    .stream()
                                    .map(ThresholdGroupEntity::getGroupId)
                                    .toList()
                    );
                    return thresholdHistory;
                })
                .toList();

    }
}
