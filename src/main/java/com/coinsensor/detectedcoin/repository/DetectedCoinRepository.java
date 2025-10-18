package com.coinsensor.detectedcoin.repository;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectedCoinRepository extends JpaRepository<DetectedCoin, Long> {
}
