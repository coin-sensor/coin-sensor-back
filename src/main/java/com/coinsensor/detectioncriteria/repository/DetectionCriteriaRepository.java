package com.coinsensor.detectioncriteria.repository;

import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectionCriteriaRepository extends JpaRepository<DetectionCriteria, Long> {
}
