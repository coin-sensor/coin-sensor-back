package com.coinsensor.coinclick.repository;

import java.util.List;
import java.util.Optional;

import com.coinsensor.coinclick.dto.response.CoinTrendDataResponse;
import com.coinsensor.coinclick.dto.response.CoinViewCountResponse;
import com.coinsensor.coinclick.entity.CoinClick;

public interface CustomCoinClickRepository {

	Optional<CoinClick> findByUuidAndDetectedCoinId(String uuid, Long detectedCoinId);

	List<CoinViewCountResponse> findTopViewedCoins(int days, int limit);

	List<CoinTrendDataResponse> findCoinsTrendData(int days, int limit);

}
