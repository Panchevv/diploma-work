package com.diploma.panchev.measurement.domain.entity;

import com.diploma.panchev.measurement.domain.MeasurementType;
import com.diploma.panchev.measurement.domain.ThresholdOperator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "threshold_history")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ThresholdHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "threshold_operator")
    @Enumerated(EnumType.STRING)
    private ThresholdOperator operator;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "measurement_type")
    @Enumerated(EnumType.STRING)
    private MeasurementType type;

    @Column(name = "threshold_value")
    private Double value;

    @Column(name = "actual_value")
    private Double actual;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "threshold_id")
    @ToString.Exclude
    private ThresholdEntity threshold;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ThresholdHistoryEntity that = (ThresholdHistoryEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
