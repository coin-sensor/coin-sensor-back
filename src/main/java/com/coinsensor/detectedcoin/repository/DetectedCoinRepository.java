package com.coinsensor.detectedcoin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detection.entity.Detection;

@Repository
public interface DetectedCoinRepository extends JpaRepository<DetectedCoin, Long>, CustomDetectedCoinRepository {

	@Query("SELECT dc FROM DetectedCoin dc " +
		"JOIN FETCH dc.detection d " +
		"JOIN FETCH dc.coin c " +
		"JOIN FETCH dc.exchangeCoin ec " +
		"JOIN FETCH ec.exchange e " +
		"WHERE dc.detection = :detection")
	List<DetectedCoin> getDetectionInfos(@Param("detection") Detection detection);
}
