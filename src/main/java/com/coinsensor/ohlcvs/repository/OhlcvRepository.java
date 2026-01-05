package com.coinsensor.ohlcvs.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.ohlcvs.entity.Ohlcv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OhlcvRepository extends JpaRepository<Ohlcv, Long>, CustomOhlcvRepository {
}