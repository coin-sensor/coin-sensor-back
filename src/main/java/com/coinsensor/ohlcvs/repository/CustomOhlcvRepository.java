package com.coinsensor.ohlcvs.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.ohlcvs.entity.Ohlcv;

public interface CustomOhlcvRepository {

	List<Ohlcv> findRecentCandles(ExchangeCoin exchangeCoin, String timeframeName, LocalDateTime startTime1,
		LocalDateTime startTime2);

	long deleteByCreatedAtBefore(LocalDateTime cutoffDate);
}