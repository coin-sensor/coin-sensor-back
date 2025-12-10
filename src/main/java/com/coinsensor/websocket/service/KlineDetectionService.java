package com.coinsensor.websocket.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.common.util.SummaryUtil;
import com.coinsensor.conditions.entity.Condition;
import com.coinsensor.conditions.repository.ConditionRepository;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.entity.Detection;
import com.coinsensor.detection.repository.DetectionRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import com.coinsensor.exchangecoin.service.ExchangeCoinService;
import com.coinsensor.ohlcvs.entity.Ohlcv;
import com.coinsensor.ohlcvs.repository.OhlcvRepository;
import com.coinsensor.timeframe.entity.Timeframe;
import com.coinsensor.timeframe.repository.TimeframeRepository;
import com.coinsensor.userreaction.dto.response.ReactionCountResponse;
import com.coinsensor.userreaction.service.UserReactionService;
import com.coinsensor.websocket.dto.KlineData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KlineDetectionService {

	private final OhlcvRepository ohlcvRepository;
	private final ExchangeCoinRepository exchangeCoinRepository;
	private final TimeframeRepository timeframeRepository;
	private final ConditionRepository conditionRepository;
	private final DetectionRepository detectionRepository;
	private final DetectedCoinRepository detectedCoinRepository;
	private final SimpMessagingTemplate messagingTemplate;
	private final ExchangeCoinService exchangeCoinService;
	private final UserReactionService userReactionService;

	@Transactional
	public void saveOhlcvDataBatch(List<KlineData> klineDataList, Exchange.Type exchangeType) {
		try {
			if (klineDataList.isEmpty())
				return;

			// Timeframe 미리 조회 (모든 데이터가 같은 timeframe)
			String timeframeName = klineDataList.get(0).getKline().getInterval();
			Timeframe timeframe = timeframeRepository.findByName(timeframeName).orElse(null);
			if (timeframe == null)
				return;

			// ExchangeCoin 일괄 조회
			List<ExchangeCoin> exchangeCoins = exchangeCoinRepository
				.findByExchangeNameAndTypeAndIsActive("binance", exchangeType, true);

			java.util.Map<String, ExchangeCoin> coinMap = exchangeCoins.stream()
				.collect(java.util.stream.Collectors.toMap(
					ec -> ec.getCoin().getCoinTicker(),
					ec -> ec
				));

			List<Ohlcv> ohlcvList = new ArrayList<>();
			for (KlineData klineData : klineDataList) {
				ExchangeCoin exchangeCoin = coinMap.get(klineData.getSymbol());
				if (exchangeCoin == null)
					continue;

				KlineData.KlineInfo kline = klineData.getKline();
				Ohlcv ohlcv = Ohlcv.from(kline, exchangeCoin, timeframe);
				ohlcvList.add(ohlcv);
			}

			if (!ohlcvList.isEmpty()) {
				ohlcvRepository.saveAll(ohlcvList);
				log.info("OHLCV 배치 저장 완료: {} {} - {} 건", exchangeType, timeframeName, ohlcvList.size());
			}
		} catch (Exception e) {
			log.error("OHLCV 배치 저장 오류: {}", e.getMessage());
		}
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

	public void detectByTimeframe(String timeframeName) {
		// 해당 조건들 조회
		List<Condition> conditions = conditionRepository.findByTimeframeName(timeframeName)
			.orElseThrow(() -> new CustomException(ErrorCode.CONDITION_NOT_FOUND));

		// 모든 조건에 대해 비동기 탐지 실행
		for (Condition condition : conditions) {
			processConditionDetection(condition, Exchange.Type.spot);
			processConditionDetection(condition, Exchange.Type.future);
		}
	}

	@Async
	@Transactional
	public void processConditionDetection(Condition condition, Exchange.Type exchangeType) {
		String timeframeName = condition.getTimeframe().getName();

		// 현재 시간을 타임프레임 시작 시간으로 정렬
		LocalDateTime currentTime = LocalDateTime.now().withSecond(0).withNano(0);
		LocalDateTime previousCandleTime1 = currentTime.minus(getTimeframeDuration(timeframeName));
		LocalDateTime previousCandleTime2 = previousCandleTime1.minus(getTimeframeDuration(timeframeName));

		// 해당 거래소의 모든 코인 조회
		List<ExchangeCoin> exchangeCoins = exchangeCoinRepository
			.findByExchangeNameAndTypeAndIsActive("binance", exchangeType, true);

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
			Exchange exchange = detectedCoins.get(0).getExchangeCoin().getExchange();
			String summary = String.format("%s %s 탐지: %d개 코인",
				exchange.getName(), timeframeName, detectedCoins.size());

			Detection detection = detectionRepository.save(
				Detection.to(condition, exchange, SummaryUtil.create(exchange, condition, detectedCoins),
					(long)detectedCoins.size(), avgChangeX, avgVolumeX));

			// DetectedCoin들에 Detection 연결 및 저장
			detectedCoins = detectedCoins.stream()
				.map(coin -> coin.withDetection(detection))
				.toList();
			detectedCoinRepository.saveAll(detectedCoins);

			// 실시간 알림 발송
			sendDetectionNotification(detection, detectedCoins);

			log.info("탐지 완료: {} {} - {}개 코인, 평균 변동률: {}%, 평균 거래량: {}배",
				exchangeType, timeframeName, detectedCoins.size(), avgChangeX, avgVolumeX);
		}
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
					.map(coin -> {
						List<ReactionCountResponse> reactionCounts = userReactionService
							.getReactionCounts("detected_coins", coin.getDetectedCoinId());
						return DetectedCoinResponse.of(coin, reactionCounts);
					})
					.toList();

				messagingTemplate.convertAndSend(topic, DetectionInfoResponse.of(detection, responses));
			}
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

	private List<DetectedCoin> filterCoinsByCategory(List<DetectedCoin> detectedCoins,
		String category, String exchangeType) {
		return switch (category) {
			case "top20" -> {
				List<String> top20Tickers = exchangeCoinService.getTopCoins(exchangeType).stream()
					.map(coin -> coin.getSymbol())
					.toList();
				yield detectedCoins.stream()
					.filter(coin -> top20Tickers.contains(coin.getExchangeCoin().getCoin().getCoinTicker()))
					.toList();
			}
			case "bottom20" -> {
				List<String> bottom20Tickers = exchangeCoinService.getBottomCoins(exchangeType).stream()
					.map(coin -> coin.getSymbol())
					.toList();
				yield detectedCoins.stream()
					.filter(coin -> bottom20Tickers.contains(coin.getExchangeCoin().getCoin().getCoinTicker()))
					.toList();
			}
			default -> detectedCoins;
		};
	}
}