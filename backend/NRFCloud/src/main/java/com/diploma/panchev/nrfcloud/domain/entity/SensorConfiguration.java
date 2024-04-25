package com.diploma.panchev.nrfcloud.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sensor_configuration")
public class SensorConfiguration {
    @Id
    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @NotNull
    @Column(name = "account_id", nullable = false)
    private String accountId;

    @NotNull
    @Column(name = "group_id", nullable = false)
    private String groupId;
}
