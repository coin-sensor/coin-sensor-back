package com.coinsensor.exchangecoin.service;

import java.util.List;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.dto.response.ExchangeCoinResponse;
import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;

public interface ExchangeCoinService {
	List<TopBottomCoinResponse> getTopCoins(String exchangeType);

	List<TopBottomCoinResponse> getBottomCoins(String exchangeType);

	List<ExchangeCoinResponse> getExchangeCoins();

	void updateExchangeCoinEnableDetection(Long exchangeCoinId);

	List<ExchangeCoin> getDetectableExchangeCoins(String exchangeName, Exchange.Type type);

}