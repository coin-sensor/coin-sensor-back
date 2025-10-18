package com.coinsensor.coinohlcv.repository;

import com.coinsensor.coinohlcv.entity.CoinOhlcv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinOhlcvRepository extends JpaRepository<CoinOhlcv, Long> {
}
