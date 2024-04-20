package com.diploma.panchev.account.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "account_user", schema = "account")
public class AccountUserEntity {
    @EmbeddedId
    private AccountUserId id;

    @MapsId("accountId")
    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "account_id", nullable = false, updatable = false, insertable = false)
    private AccountEntity account;

    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
