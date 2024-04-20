package com.diploma.panchev.account.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "account_group_device")
public class AccountGroupDeviceEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "account_device_id")
    private AccountDeviceEntity accountDevice;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "account_group_id")
    private AccountGroupEntity group;

    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}
