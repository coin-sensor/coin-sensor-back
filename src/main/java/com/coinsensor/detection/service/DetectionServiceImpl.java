package com.coinsensor.detection.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.entity.Detection;
import com.coinsensor.detection.repository.DetectionRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;
import com.coinsensor.exchangecoin.service.ExchangeCoinService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetectionServiceImpl implements DetectionService {

	private final DetectionRepository detectionRepository;
	private final DetectedCoinRepository detectedCoinRepository;
	private final ExchangeCoinService exchangeCoinService;

	@Override
	public List<DetectionInfoResponse> getDetections(String exchange, String exchangeType, String coinCategory,
		String timeframe) {
		LocalDateTime startTime = getStartTimeByTimeframe(timeframe);
		Exchange.Type type = Exchange.Type.valueOf(exchangeType);

		return detectionRepository.findByExchangeAndTimeframeAndAfterTime(exchange, type, timeframe, startTime)
			.stream()
			.map(detection -> {
				List<DetectedCoin> detectedCoins = detectedCoinRepository.findByDetection(detection);

				return filterDetectionByCoinCategory(detection, detectedCoins, coinCategory, exchangeType);

			})
			.filter(Objects::nonNull)
			.toList();
	}

	private LocalDateTime getStartTimeByTimeframe(String timeframe) {
		LocalDateTime now = LocalDateTime.now();
		return switch (timeframe) {
			case "1m" -> now.minusMinutes(30);
			case "5m" -> now.minusHours(1);
			case "15m" -> now.minusHours(4);
			case "1h" -> now.minusHours(24);
			case "4h" -> now.minusDays(1);
			case "1d" -> now.minusDays(7);
			default -> now.minusHours(1);
		};
	}

	private DetectionInfoResponse filterDetectionByCoinCategory(Detection detection,
		List<DetectedCoin> detectedCoins, String coinCategory, String exchangeType) {
		switch (coinCategory) {
			case "top20":
				List<String> top20Tickers = exchangeCoinService.getTopCoins(exchangeType).stream()
					.map(TopBottomCoinResponse::getSymbol)
					.toList();
				List<DetectedCoin> detectedTop20Coin = detectedCoins.stream()
					.filter(coin -> top20Tickers.contains(coin.getExchangeCoin().getCoin().getCoinTicker()))
					.toList();
				return !detectedTop20Coin.isEmpty() ? DetectionInfoResponse.of(detection, detectedTop20Coin) : null;

			case "bottom20":
				List<String> bottom20Tickers = exchangeCoinService.getBottomCoins(exchangeType).stream()
					.map(TopBottomCoinResponse::getSymbol)
					.toList();
				List<DetectedCoin> detectedBottom20Coin = detectedCoins.stream()
					.filter(coin -> bottom20Tickers.contains(coin.getExchangeCoin().getCoin().getCoinTicker()))
					.toList();
				return !detectedBottom20Coin.isEmpty() ? DetectionInfoResponse.of(detection, detectedBottom20Coin) :
					null;

			default:
				return DetectionInfoResponse.of(detection, detectedCoins);
		}
	}

}