package com.diploma.panchev.measurement.service.impl;

import com.diploma.panchev.measurement.domain.*;
import com.diploma.panchev.measurement.domain.entity.DeviceMeasurementEntity;
import com.diploma.panchev.measurement.repository.DeviceMeasurementRepository;
import com.diploma.panchev.measurement.service.MeasurementService;
import com.diploma.panchev.measurement.service.mapper.MeasurementMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class MeasurementServiceImpl implements MeasurementService {
    private final static MeasurementMapper MAPPER = Mappers.getMapper(MeasurementMapper.class);

    private final DeviceMeasurementRepository repository;

    public MeasurementServiceImpl(DeviceMeasurementRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DeviceMeasurementEntity> getLastMeasurements(String deviceId) {
        log.trace("getLastMeasurements: deviceId={}", deviceId);
        return Stream.of(MeasurementType.values())
                .map(type -> repository.findFirstByDeviceIdAndTypeOrderByCreatedAtDesc(deviceId, type))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .peek(current -> log.trace("measurement: {}", current))
                .collect(Collectors.toList());
    }

    @Override
    public Relay<DeviceMeasurementEntity> getMeasurementHistory(String deviceId, MeasurementType type, OffsetDateTime from, OffsetDateTime to, Long fromId, Integer pageSize) {
        Page<DeviceMeasurementEntity> deviceHistory = this.repository.findDeviceHistory(deviceId, type, from, to, fromId, PageRequest.of(0, pageSize));

        long count = deviceHistory.getSize();
        long totalElements = this.repository.countDeviceHistory(deviceId, type, from, to);
        Relay<DeviceMeasurementEntity> relay = new Relay<>();
        relay.setItems(deviceHistory.getContent());
        RelayPageInfo pageInfo = new RelayPageInfo();
        pageInfo.setTotalElements(totalElements);
        pageInfo.setCount(count);
        pageInfo.setHasNextPage(deviceHistory.getNumber() + 1 < deviceHistory.getTotalPages());
        pageInfo.setHasPreviousPage(totalElements > deviceHistory.getTotalElements());
        relay.setPageInfo(pageInfo);
        return relay;
    }

    @Override
    public DeviceMeasurement createMeasurement(UUID accountId, UUID groupId, String deviceId, String messageId, Measurement measurement, OffsetDateTime when) {
        DeviceMeasurementEntity entity = new DeviceMeasurementEntity();
        entity.setDeviceId(deviceId);
        entity.setAccountId(accountId);
        entity.setGroupId(groupId);
        entity.setType(measurement.getType());
        entity.setValue(measurement.getValue());
        entity.setCreatedAt(when);
        entity.setMessageId(messageId);
        return MAPPER.map(repository.save(entity));
    }

    @Override
    public Optional<DeviceMeasurementEntity> getLastMeasurement(String deviceId, MeasurementType type) {
        return repository.findFirstByDeviceIdAndTypeOrderByCreatedAtDesc(deviceId, type);
    }
}
