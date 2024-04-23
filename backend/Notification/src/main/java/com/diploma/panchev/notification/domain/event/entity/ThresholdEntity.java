package com.diploma.panchev.notification.domain.event.entity;

import com.diploma.panchev.notification.domain.event.MeasurementType;
import com.diploma.panchev.notification.domain.event.ThresholdOperator;
import com.diploma.panchev.notification.utils.convertor.MeasurementTypeConverter;
import com.diploma.panchev.notification.utils.convertor.ThresholdOperatorConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "threshold")
public class ThresholdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Size(max = 255)
    @Column(name = "threshold_name")
    private String thresholdName;

    @Column(name = "measurement_type_id")
    @Convert(converter = MeasurementTypeConverter.class)
    private MeasurementType type;

    @Column(name = "threshold_operator_id")
    @Convert(converter = ThresholdOperatorConverter.class)
    private ThresholdOperator operator;

    @Column(name = "value")
    private Double value;

    @Column(name = "threshold")
    private Double threshold;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "event_id")
    @ToString.Exclude
    private EventEntity event;

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
}
