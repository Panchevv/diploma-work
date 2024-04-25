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
@Table(name = "mqtt_configuration")
public class MqttConfiguration {
    @Id
    @Column(name = "account_id", nullable = false)
    private String id;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

    @Size(max = 255)
    @Column(name = "host")
    private String host;

    @Size(max = 255)
    @Column(name = "client_id")
    private String clientId;

    @Size(max = 255)
    @Column(name = "prefix")
    private String prefix;

    @Column(name = "ca_cert")
    private byte[] caCert;

    @Column(name = "client_cert")
    private byte[] clientCert;

    @Column(name = "private_key")
    private byte[] privateKey;

    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
