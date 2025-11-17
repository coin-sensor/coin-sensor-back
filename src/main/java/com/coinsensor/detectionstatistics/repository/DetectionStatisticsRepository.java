package com.coinsensor.detectionstatistics.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.coinsensor.detectionstatistics.entity.DetectionStatistics;

public interface DetectionStatisticsRepository extends JpaRepository<DetectionStatistics, Long> {

	@Query("SELECT ds FROM DetectionStatistics ds WHERE ds.timeframe.timeframeLabel = :timeframeLabel AND ds.createdAt BETWEEN :startTime AND :endTime ORDER BY ds.createdAt")
	List<DetectionStatistics> findByTimeframeLabelAndCreatedAtBetween(
		@Param("timeframeLabel") String timeframeLabel,
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime
	);

	@Query("SELECT ds FROM DetectionStatistics ds WHERE ds.createdAt >= :startTime ORDER BY ds.createdAt")
	List<DetectionStatistics> findRecentStatistics(@Param("startTime") LocalDateTime startTime);

	@Query("SELECT SUM(ds.count) FROM DetectionStatistics ds WHERE ds.createdAt BETWEEN :startTime AND :endTime")
	Long getTotalDetectionCount(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

	@Query("SELECT AVG(ds.volatilityAvg) FROM DetectionStatistics ds WHERE ds.createdAt BETWEEN :startTime AND :endTime")
	Double getAverageVolatility(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}