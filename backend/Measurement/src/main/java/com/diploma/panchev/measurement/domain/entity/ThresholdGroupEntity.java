package com.diploma.panchev.measurement.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "threshold_group")
public class ThresholdGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "threshold_id")
    private UUID thresholdId;

    @Column(name = "group_id")
    private String groupId;
}
