package com.diploma.panchev.measurement.domain.entity;

import com.diploma.panchev.measurement.domain.MeasurementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "measurement")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DeviceMeasurementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "measurement_id")
    private Long id;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "measurement_type")
    @Enumerated(EnumType.STRING)
    private MeasurementType type;

    @Column(name = "measurement_value")
    private Double value;

    @Column(name = "created_on")
    private OffsetDateTime createdAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DeviceMeasurementEntity that = (DeviceMeasurementEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
