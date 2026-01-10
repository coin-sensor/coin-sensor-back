package com.coinsensor.detection.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coinsensor.detection.entity.Detection;

@Repository
public interface DetectionRepository extends JpaRepository<Detection, Long>, CustomDetectionRepository {

}
