package com.coinsensor.ohlcvs.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.coinsensor.ohlcvs.entity.Ohlcv;

import com.coinsensor.exchangecoin.entity.ExchangeCoin;

public interface CustomOhlcvRepository {

    List<Ohlcv> findRecentCandles(ExchangeCoin exchangeCoin, String timeframeName, LocalDateTime startTime1, LocalDateTime startTime2);
    
    long deleteByCreatedAtBefore(LocalDateTime cutoffDate);
}