package com.coinsensor.exchangecoin.service;

import java.util.List;

import com.coinsensor.exchangecoin.dto.response.ExchangeCoinResponse;
import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;

public interface ExchangeCoinService {
	List<TopBottomCoinResponse> getTopCoins(String exchangeType);

	List<TopBottomCoinResponse> getBottomCoins(String exchangeType);

	List<ExchangeCoinResponse> getExchangeCoins();

	void updateExchangeCoinEnableDetection(Long exchangeCoinId);
}