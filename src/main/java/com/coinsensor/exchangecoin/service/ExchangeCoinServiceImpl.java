package com.coinsensor.exchangecoin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExchangeCoinServiceImpl implements ExchangeCoinService {

	private final BinanceWebSocketService binanceWebSocketService;

	@Override
	public List<TopBottomCoinResponse> getTopCoins(String exchangeType) {
		return "spot".equals(exchangeType) ?
			binanceWebSocketService.getSpotTopCoins() :
			binanceWebSocketService.getFuturesTopCoins();
	}

	@Override
	public List<TopBottomCoinResponse> getBottomCoins(String exchangeType) {
		return "spot".equals(exchangeType) ?
			binanceWebSocketService.getSpotBottomCoins() :
			binanceWebSocketService.getFuturesBottomCoins();
	}
}