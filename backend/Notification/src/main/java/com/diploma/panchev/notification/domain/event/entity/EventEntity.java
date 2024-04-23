package com.diploma.panchev.notification.domain.event.entity;

import jakarta.persistence.*;
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
@Table(name = "event")
public class EventEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "device_id")
    private String  deviceId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "seen_at")
    private OffsetDateTime seenAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @OneToOne(mappedBy = "event")
    private ThresholdEntity threshold;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        EventEntity that = (EventEntity) o;
        return getId() != null && Objects.equals(
                getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
