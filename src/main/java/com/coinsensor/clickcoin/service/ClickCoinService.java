package com.coinsensor.clickcoin.service;

import java.util.List;

import com.coinsensor.clickcoin.dto.response.CoinTrendDataResponse;
import com.coinsensor.clickcoin.dto.response.CoinViewCountResponse;

public interface ClickCoinService {

	List<CoinViewCountResponse> getTopViewedCoins(int days, int limit);



	List<CoinTrendDataResponse> getCoinsTrendData(int days, int limit);

}