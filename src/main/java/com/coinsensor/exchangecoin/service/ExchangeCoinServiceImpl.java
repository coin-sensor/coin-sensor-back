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
	public List<TopBottomCoinResponse> getTopCoins(String market) {
		return "spot".equals(market) ?
			binanceWebSocketService.getSpotTopCoins() :
			binanceWebSocketService.getFuturesTopCoins();
	}

	@Override
	public List<TopBottomCoinResponse> getBottomCoins(String market) {
		return "spot".equals(market) ?
			binanceWebSocketService.getSpotBottomCoins() :
			binanceWebSocketService.getFuturesBottomCoins();
	}
}