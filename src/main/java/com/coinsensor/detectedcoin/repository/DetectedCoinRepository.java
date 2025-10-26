package com.coinsensor.detectedcoin.repository;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.exchange.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DetectedCoinRepository extends JpaRepository<DetectedCoin, Long> {
    
    @Query("SELECT dc FROM DetectedCoin dc " +
           "JOIN FETCH dc.detectionGroup dg " +
           "JOIN FETCH dg.exchange e " +
           "WHERE e.name = :exchangeName " +
           "AND e.exchangeType = :exchangeType " +
           "AND dg.detectedAt >= :startTime " +
           "AND dg.detectedAt < :endTime")
    List<DetectedCoin> findByExchangeNameAndTypeAndTime(
        @Param("exchangeName") String exchangeName,
        @Param("exchangeType") Exchange.ExchangeType exchangeType,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    List<DetectedCoin> findByDetectionGroup_DetectionGroupId(Long detectionGroupId);
}
