package com.coinsensor.exchangecoin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.exchangecoin.dto.response.ExchangeCoinResponse;
import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ExchangeCoinServiceImpl implements ExchangeCoinService {

	private final BinanceWebSocketService binanceWebSocketService;
	private final ExchangeCoinRepository exchangeCoinRepository;

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

	@Override
	@Transactional(readOnly = true)
	public List<ExchangeCoinResponse> getExchangeCoins() {
		return exchangeCoinRepository.findExchangeCoins()
			.stream()
			.map(ExchangeCoinResponse::from)
			.toList();
	}

	@Override
	public void updateExchangeCoinEnableDetection(Long exchangeCoinId) {
		ExchangeCoin exchangeCoin = exchangeCoinRepository.findById(exchangeCoinId)
			.orElseThrow(() -> new CustomException(ErrorCode.EXCHANGE_COIN_NOT_FOUND));

		exchangeCoin.setEnableDetection(!exchangeCoin.getEnableDetection());
	}
}