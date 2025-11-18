package com.coinsensor.detection.repository;

import com.coinsensor.detection.entity.Detection;
import com.coinsensor.exchange.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetectionRepository extends JpaRepository<Detection, Long> {
    
    @Query("SELECT dg FROM Detection dg " +
           "JOIN FETCH dg.exchange e " +
           "JOIN FETCH dg.condition c " +
           "JOIN FETCH c.timeframe tf " +
           "WHERE e.name = :exchangeName " +
           "AND e.type = :exchangeType " +
           "AND tf.name = :timeframeName " +
           "AND dg.detectedAt >= :startTime " +
           "AND dg.detectedAt < :endTime")
    Optional<Detection> findByExchangeAndTimeframeAndTime(
        @Param("exchangeName") String exchangeName,
        @Param("exchangeType") Exchange.Type exchangeType,
        @Param("timeframeName") String timeframeName,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    @Query("SELECT dg FROM Detection dg " +
           "JOIN FETCH dg.condition c " +
           "JOIN FETCH c.timeframe tf " +
           "WHERE tf.name = :timeframeName " +
           "AND dg.detectedAt >= :startTime " +
           "ORDER BY dg.detectedAt DESC")
    List<Detection> findByTimeframeAndAfterTime(
        @Param("timeframeName") String timeframeName,
        @Param("startTime") LocalDateTime startTime
    );
    
    @Query("SELECT dg FROM Detection dg " +
           "JOIN FETCH dg.exchange e " +
           "JOIN FETCH dg.condition c " +
           "JOIN FETCH c.timeframe tf " +
           "WHERE e.name = :exchangeName " +
           "AND e.type = :exchangeType " +
           "AND tf.name = :timeframeName " +
           "AND dg.detectedAt >= :startTime " +
           "ORDER BY dg.detectedAt DESC")
    List<Detection> findByExchangeAndTimeframeAndAfterTime(
        @Param("exchangeName") String exchangeName,
        @Param("exchangeType") Exchange.Type exchangeType,
        @Param("timeframeName") String timeframeName,
        @Param("startTime") LocalDateTime startTime
    );
}
