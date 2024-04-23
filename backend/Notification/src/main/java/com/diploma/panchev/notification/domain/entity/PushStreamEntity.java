package com.diploma.panchev.notification.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "push_stream")
public class PushStreamEntity {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Size(max = 64)
    @Column(name = "account_id", length = 64)
    private String accountId;

    @Size(max = 64)
    @Column(name = "group_id", length = 64)
    private String groupId;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
