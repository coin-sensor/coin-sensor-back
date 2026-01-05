package com.coinsensor.coinclick.service;

import java.util.List;

import com.coinsensor.coinclick.dto.response.CoinTrendDataResponse;
import com.coinsensor.coinclick.dto.response.CoinViewCountResponse;

public interface CoinClickService {

	List<CoinViewCountResponse> getTopViewedCoins(int days, int limit);

	List<CoinTrendDataResponse> getCoinsTrendData(int days, int limit);

}