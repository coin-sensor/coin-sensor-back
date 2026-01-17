package com.coinsensor.detectedcoin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coinsensor.detectedcoin.entity.DetectedCoin;

@Repository
public interface DetectedCoinRepository extends JpaRepository<DetectedCoin, Long>, CustomDetectedCoinRepository {
}
