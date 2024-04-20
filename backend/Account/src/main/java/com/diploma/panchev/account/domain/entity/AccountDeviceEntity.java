package com.diploma.panchev.account.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "account_device")
public class AccountDeviceEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @OneToOne(mappedBy = AccountGroupDeviceEntity_.ACCOUNT_DEVICE, optional = false)
    private AccountGroupDeviceEntity group;

    @Size(max = 64)
    @NotNull
    @Column(name = "device_id", nullable = false, length = 64)
    private String deviceId;

    @Size(max = 255)
    @NotNull
    @Column(name = "device_name", nullable = false)
    private String name;

    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
