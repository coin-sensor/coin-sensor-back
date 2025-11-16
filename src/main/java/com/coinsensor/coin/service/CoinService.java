package com.coinsensor.coin.service;

import java.util.List;

import com.coinsensor.coin.dto.response.CoinResponse;

public interface CoinService {
	List<CoinResponse> getAllCoins();

	CoinResponse getCoinById(Long coinId);

}
