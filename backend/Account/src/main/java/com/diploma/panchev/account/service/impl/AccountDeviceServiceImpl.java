package com.diploma.panchev.account.service.impl;

import com.diploma.panchev.account.domain.AccountDevice;
import com.diploma.panchev.account.mapper.database.AccountDeviceMapper;
import com.diploma.panchev.account.repository.AccountDeviceRepository;
import com.diploma.panchev.account.service.AccountDeviceService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class AccountDeviceServiceImpl implements AccountDeviceService {
    private final static AccountDeviceMapper MAPPER = Mappers.getMapper(AccountDeviceMapper.class);

    private final AccountDeviceRepository accountDeviceRepository;

    public AccountDeviceServiceImpl(AccountDeviceRepository accountDeviceRepository) {
        this.accountDeviceRepository = accountDeviceRepository;
    }

    @Override
    public Optional<AccountDevice> getAccountDevice(UUID accountId, String deviceId) {
        return this.accountDeviceRepository.findByAccountIdAndDeviceId(accountId, deviceId)
                .map(MAPPER::map);
    }
}
