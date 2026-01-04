package com.coinsensor.detection.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detection.dto.response.DetectionChartResponse;
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
				return !detectedTop20Coin.isEmpty() ? DetectionInfoResponse.of(detection, detectedTop20Coin.stream().map(DetectedCoinResponse::from).toList()) : null;

			case "bottom20":
				List<String> bottom20Tickers = exchangeCoinService.getBottomCoins(exchangeType).stream()
					.map(TopBottomCoinResponse::getSymbol)
					.toList();
				List<DetectedCoin> detectedBottom20Coin = detectedCoins.stream()
					.filter(coin -> bottom20Tickers.contains(coin.getExchangeCoin().getCoin().getCoinTicker()))
					.toList();
				return !detectedBottom20Coin.isEmpty() ? DetectionInfoResponse.of(detection, detectedBottom20Coin.stream().map(DetectedCoinResponse::from).toList()) :
					null;

			default:
				return DetectionInfoResponse.of(detection, detectedCoins.stream().map(DetectedCoinResponse::from).toList());
		}
	}

	@Override
	public DetectionChartResponse getDetectionChart(String timeframe, LocalDateTime startTime, LocalDateTime endTime) {
		List<Detection> detections = detectionRepository.findByTimeframeBetween(timeframe, startTime, endTime);

		Map<String, Long> detectionCounts = detections.stream()
			.collect(Collectors.groupingBy(
				detection -> formatDateTime(detection.getDetectedAt(), timeframe),
				Collectors.summingLong(Detection::getDetectionCount)
			));

		List<String> labels = new ArrayList<>(detectionCounts.keySet());
		labels.sort(String::compareTo);

		List<Integer> data = labels.stream()
			.map(label -> detectionCounts.getOrDefault(label, 0L).intValue())
			.toList();

		DetectionChartResponse.Dataset dataset = new DetectionChartResponse.Dataset("탐지 수", data);
		return new DetectionChartResponse(labels, List.of(dataset));
	}

	private String formatDateTime(LocalDateTime dateTime, String timeframe) {
		DateTimeFormatter formatter = switch (timeframe) {
			case "1m", "5m", "15m" -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			case "1h", "4h" -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");
			case "1d" -> DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00");
			default -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		};
		return dateTime.format(formatter);
	}


}