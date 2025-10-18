package com.coinsensor.detectiongroup.repository;

import com.coinsensor.detectiongroup.entity.DetectionGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectionGroupRepository extends JpaRepository<DetectionGroup, Long> {
}
