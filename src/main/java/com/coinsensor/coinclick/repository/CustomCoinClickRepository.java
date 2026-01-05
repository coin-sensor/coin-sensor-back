package com.coinsensor.coinclick.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.coinsensor.coinclick.dto.response.CoinTrendDataResponse;
import com.coinsensor.coinclick.dto.response.CoinViewCountResponse;
import com.coinsensor.coinclick.entity.CoinClick;

public interface CustomCoinClickRepository {

	Optional<CoinClick> findByUuidAndDetectedCoinId(String uuid, Long detectedCoinId);

	List<CoinViewCountResponse> findTopViewedCoins(LocalDateTime startTime, int limit);

	List<CoinTrendDataResponse> findCoinsTrendData(LocalDateTime startTime, int limit);

}
