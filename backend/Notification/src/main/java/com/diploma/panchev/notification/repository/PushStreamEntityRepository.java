package com.diploma.panchev.notification.repository;

import com.diploma.panchev.notification.domain.entity.PushStreamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PushStreamEntityRepository extends JpaRepository<PushStreamEntity, UUID> {
}
