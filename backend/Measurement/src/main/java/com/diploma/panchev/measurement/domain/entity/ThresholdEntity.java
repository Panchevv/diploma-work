package com.diploma.panchev.measurement.domain.entity;

import com.diploma.panchev.measurement.domain.MeasurementType;
import com.diploma.panchev.measurement.domain.ThresholdOperator;
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

@Entity
@Table(name = "threshold")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ThresholdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "threshold_operator")
    @Enumerated(EnumType.STRING)
    private ThresholdOperator operator;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "measurement_type")
    @Enumerated(EnumType.STRING)
    private MeasurementType type;

    @Column(name = "threshold_value")
    private Double value;

    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ThresholdEntity that = (ThresholdEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    public boolean evaluate(double value) {
        return this.operator.evaluate(value, this.getValue());
    }
}
