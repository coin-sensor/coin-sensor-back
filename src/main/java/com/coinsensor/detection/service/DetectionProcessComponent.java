package com.coinsensor.detection.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.util.SummaryUtil;
import com.coinsensor.conditions.entity.Condition;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.entity.Detection;
import com.coinsensor.detection.repository.DetectionRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.service.ExchangeCoinService;
import com.coinsensor.ohlcvs.entity.Ohlcv;
import com.coinsensor.ohlcvs.repository.OhlcvRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DetectionProcessComponent {
	private final OhlcvRepository ohlcvRepository;
	private final DetectionRepository detectionRepository;
	private final DetectedCoinRepository detectedCoinRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final ExchangeCoinService exchangeCoinService;

	@Async
	public void processConditionDetection(Condition condition, Exchange.Type exchangeType) {
		String timeframeName = condition.getTimeframe().getName();

		// 현재 시간을 타임프레임 시작 시간으로 정렬
		LocalDateTime currentTime = LocalDateTime.now().withSecond(0).withNano(0);
		LocalDateTime previousCandleTime1 = currentTime.minus(getTimeframeDuration(timeframeName));
		LocalDateTime previousCandleTime2 = previousCandleTime1.minus(getTimeframeDuration(timeframeName));

		// 해당 거래소의 모든 코인 조회
		List<ExchangeCoin> exchangeCoins = exchangeCoinService
			.getDetectableExchangeCoins("binance", exchangeType);

		List<DetectedCoin> detectedCoins = new ArrayList<>();
		BigDecimal totalChangeX = BigDecimal.ZERO;
		BigDecimal totalVolumeX = BigDecimal.ZERO;

		for (ExchangeCoin exchangeCoin : exchangeCoins) {
			// 2개의 캔들을 한번에 조회
			List<Ohlcv> candles = ohlcvRepository.findRecentCandles(
				exchangeCoin, timeframeName, previousCandleTime1, previousCandleTime2);

			// 2개의 캔들이 모두 없으면 패스
			if (candles.size() < 2) {
				continue;
			}

			Ohlcv previousCandle1 = candles.get(0); // 전봉 (최근)
			Ohlcv previousCandle2 = candles.get(1); // 전전봉

			// 변동률 및 거래량 배율 계산
			BigDecimal changeX = calculateChangeX(previousCandle1);
			BigDecimal volumeX = calculateVolumeX(previousCandle2, previousCandle1);

			// 종가가 시가보다 낮으면 음수로 변경
			if (previousCandle1.getClose().compareTo(previousCandle1.getOpen()) < 0) {
				changeX = changeX.negate();
			}

			// 조건 체크 (절대값으로 비교)
			if (isDetectionConditionMet(condition, changeX.abs(), volumeX)) {
				DetectedCoin detectedCoin = DetectedCoin.to(exchangeCoin, changeX, volumeX,
					previousCandle1.getHigh(), previousCandle1.getLow());
				detectedCoins.add(detectedCoin);

				totalChangeX = totalChangeX.add(changeX);
				totalVolumeX = totalVolumeX.add(volumeX);
			}
		}

		if (!detectedCoins.isEmpty()) {
			// 평균 계산
			BigDecimal avgChangeX = totalChangeX.divide(BigDecimal.valueOf(detectedCoins.size()), 2,
				RoundingMode.HALF_UP);
			BigDecimal avgVolumeX = totalVolumeX.divide(BigDecimal.valueOf(detectedCoins.size()), 2,
				RoundingMode.HALF_UP);

			// Detection 생성
			Exchange exchange = detectedCoins.getFirst().getExchangeCoin().getExchange();

			Detection detection = detectionRepository.save(
				Detection.to(condition, exchange, SummaryUtil.create(exchange, condition, detectedCoins),
					(long)detectedCoins.size(), avgChangeX, avgVolumeX));

			// DetectedCoin 들에 Detection 연결 및 저장
			detectedCoins = detectedCoins.stream()
				.map(coin -> coin.withDetection(detection))
				.toList();
			detectedCoinRepository.saveAll(detectedCoins);

			// 실시간 알림 발송
			sendDetectionNotification(detection, detectedCoins);

			log.info("[{}-{}] {} 탐지 완료: {}개 코인, 평균 변동률: {}%, 평균 거래량: {}배",
				exchange.getName(), exchangeType, timeframeName, detectedCoins.size(), avgChangeX, avgVolumeX);
		}
	}

	private Duration getTimeframeDuration(String timeframe) {
		return switch (timeframe) {
			case "1m" -> Duration.ofMinutes(1);
			case "5m" -> Duration.ofMinutes(5);
			case "15m" -> Duration.ofMinutes(15);
			case "1h" -> Duration.ofHours(1);
			case "4h" -> Duration.ofHours(4);
			case "1d" -> Duration.ofDays(1);
			default -> Duration.ZERO;
		};
	}

	private BigDecimal calculateChangeX(Ohlcv previousCandle1) {
		if (previousCandle1.getLow().compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		// 고점과 저점의 변동률 계산 (high/low - 1) * 100
		return previousCandle1.getHigh().divide(previousCandle1.getLow(), 4, RoundingMode.HALF_UP)
			.subtract(BigDecimal.ONE)
			.multiply(BigDecimal.valueOf(100));
	}

	private BigDecimal calculateVolumeX(Ohlcv previousCandle2, Ohlcv previousCandle1) {
		if (previousCandle2.getVolume().compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		return previousCandle1.getVolume()
			.divide(previousCandle2.getVolume(), 2, RoundingMode.HALF_UP);
	}

	private boolean isDetectionConditionMet(Condition condition, BigDecimal changeX, BigDecimal volumeX) {
		return changeX.doubleValue() >= condition.getChangeX().doubleValue() &&
			volumeX.compareTo(condition.getVolumeX()) >= 0;
	}

	private List<DetectedCoin> filterCoinsByCategory(List<DetectedCoin> detectedCoins,
		String category, String exchangeType) {
		return switch (category) {
			case "top20" -> {
				List<String> top20Tickers = exchangeCoinService.getTopCoins(exchangeType).stream()
					.map(TopBottomCoinResponse::getSymbol)
					.toList();
				yield detectedCoins.stream()
					.filter(coin -> top20Tickers.contains(coin.getExchangeCoin().getCoin().getCoinTicker()))
					.toList();
			}
			case "bottom20" -> {
				List<String> bottom20Tickers = exchangeCoinService.getBottomCoins(exchangeType).stream()
					.map(TopBottomCoinResponse::getSymbol)
					.toList();
				yield detectedCoins.stream()
					.filter(coin -> bottom20Tickers.contains(coin.getExchangeCoin().getCoin().getCoinTicker()))
					.toList();
			}
			default -> detectedCoins;
		};
	}

	private void sendDetectionNotification(Detection detection, List<DetectedCoin> detectedCoins) {
		String timeframe = detection.getCondition().getTimeframe().getName();
		String exchangeName = detection.getExchange().getName();
		String exchangeType = detection.getExchange().getType().name();

		List<String> coinCategories = List.of("all", "top20", "bottom20");

		for (String coinCategory : coinCategories) {
			List<DetectedCoin> filteredCoins = filterCoinsByCategory(detectedCoins, coinCategory, exchangeType);

			if (!filteredCoins.isEmpty()) {
				String topic = String.format(
					"/topic/detections?exchange=%s&exchangeType=%s&coinCategory=%s&timeframe=%s",
					exchangeName, exchangeType, coinCategory, timeframe);

				List<DetectedCoinResponse> responses = filteredCoins.stream()
					.map(DetectedCoinResponse::from)
					.toList();

				messagingTemplate.convertAndSend(topic, DetectionInfoResponse.of(detection, responses));
			}
		}
	}
}