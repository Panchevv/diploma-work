package com.diploma.panchev.nrfcloud.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "api_configuration")
public class ApiConfiguration {
    @Id
    @Column(name = "account_id", nullable = false)
    private String id;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Size(max = 255)
    @Column(name = "bearer_token")
    private String bearerToken;

    @Size(max = 255)
    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
