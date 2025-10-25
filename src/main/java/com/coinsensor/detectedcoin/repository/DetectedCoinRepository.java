package com.coinsensor.detectedcoin.repository;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.exchangecoin.entity.ExchangeCoin.ExchangeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetectedCoinRepository extends JpaRepository<DetectedCoin, Long> {
    
    @Query("SELECT dc FROM DetectedCoin dc " +
           "JOIN FETCH dc.exchangeCoin ec " +
           "JOIN FETCH dc.detectionGroup dg " +
           "WHERE dg.detectedAt >= :startTime " +
           "AND dg.detectedAt < :endTime " +
           "AND ec.exchangeType = :exchangeType")
    List<DetectedCoin> findByDetectionTimeRangeAndExchangeType(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("exchangeType") ExchangeType exchangeType
    );
    
    @Query("SELECT dc FROM DetectedCoin dc " +
           "JOIN FETCH dc.exchangeCoin ec " +
           "JOIN FETCH dc.detectionGroup dg " +
           "JOIN FETCH dg.detectionCriteria dcr " +
           "JOIN FETCH dcr.timeframe tf " +
           "WHERE dg.detectedAt >= :startTime " +
           "AND dg.detectedAt < :endTime " +
           "AND ec.exchangeType = :exchangeType " +
           "AND tf.timeframeLabel = :timeframeLabel")
    List<DetectedCoin> findByTimeframeAndExchangeType(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("timeframeLabel") String timeframeLabel,
        @Param("exchangeType") ExchangeType exchangeType
    );
}
