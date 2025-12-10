package com.coinsensor.clickcoin.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.coinsensor.clickcoin.dto.response.CoinTrendDataResponse;
import com.coinsensor.clickcoin.dto.response.CoinViewCountResponse;
import com.coinsensor.clickcoin.entity.ClickCoin;

public interface CustomClickCoinRepository {

	Optional<ClickCoin> findByUuidAndDetectedCoinId(String uuid, Long detectedCoinId);

	List<CoinViewCountResponse> findTopViewedCoins(LocalDateTime startTime, int limit);

	List<CoinTrendDataResponse> findCoinsTrendData(LocalDateTime startTime, int limit);

}
