package com.coinsensor.detectedcoin.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detection.entity.Detection;
import com.coinsensor.exchange.entity.Exchange;

@Repository
public interface DetectedCoinRepository extends JpaRepository<DetectedCoin, Long> {

	@Query("SELECT dc FROM DetectedCoin dc " +
		"JOIN FETCH dc.detection dg " +
		"JOIN FETCH dg.exchange e " +
		"WHERE e.name = :exchangeName " +
		"AND e.type = :exchangeType " +
		"AND dg.detectedAt >= :startTime " +
		"AND dg.detectedAt < :endTime")
	List<DetectedCoin> findByExchangeNameAndTypeAndTime(
		@Param("exchangeName") String exchangeName,
		@Param("exchangeType") Exchange.Type exchangeType,
		@Param("startTime") LocalDateTime startTime,
		@Param("endTime") LocalDateTime endTime
	);

	@Query("SELECT dc FROM DetectedCoin dc " +
		"JOIN FETCH dc.detection d " +
		"JOIN FETCH dc.coin c " +
		"JOIN FETCH dc.exchangeCoin ec " +
		"JOIN FETCH ec.exchange e " +
		"WHERE dc.detection = :detection")
	List<DetectedCoin> findByDetection(@Param("detection") Detection detection);
}
