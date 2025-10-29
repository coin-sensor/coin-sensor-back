package com.coinsensor.detectiongroup.repository;

import com.coinsensor.detectiongroup.entity.DetectionGroup;
import com.coinsensor.exchange.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface DetectionGroupRepository extends JpaRepository<DetectionGroup, Long> {
    
    @Query("SELECT dg FROM DetectionGroup dg " +
           "JOIN FETCH dg.exchange e " +
           "JOIN FETCH dg.detectionCriteria dc " +
           "JOIN FETCH dc.timeframe tf " +
           "WHERE e.name = :exchangeName " +
           "AND e.type = :exchangeType " +
           "AND tf.timeframeLabel = :timeframeLabel " +
           "AND dg.detectedAt >= :startTime " +
           "AND dg.detectedAt < :endTime")
    Optional<DetectionGroup> findByExchangeAndTimeframeAndTime(
        @Param("exchangeName") String exchangeName,
        @Param("exchangeType") Exchange.Type exchangeType,
        @Param("timeframeLabel") String timeframeLabel,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
