package com.diploma.panchev.account.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="account_group_device_history")
public class AccountGroupDeviceHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_group_device_id")
    private Long id;

    @Column(name = "account_id")
    private UUID accountId;

    @Size(max = 64)
    @Column(name = "device_id", length = 64)
    private String deviceId;

    @ManyToOne
    @JoinColumn(name = "account_group_id")
    private AccountGroupEntity group;

    @Column(name="active_from")
    private OffsetDateTime activeFrom;

    @Column(name="active_to")
    private OffsetDateTime activeTo;

    @Column(name = "modified_on")
    private OffsetDateTime modifiedOn;

    @Column(name = "created_on")
    private OffsetDateTime createdOn;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        AccountGroupDeviceHistoryEntity that = (AccountGroupDeviceHistoryEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
