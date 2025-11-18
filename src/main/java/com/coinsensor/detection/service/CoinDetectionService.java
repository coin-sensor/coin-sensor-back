package com.coinsensor.detection.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.common.util.SummaryUtil;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.entity.Detection;
import com.coinsensor.detection.repository.DetectionRepository;
import com.coinsensor.conditions.entity.Condition;
import com.coinsensor.conditions.repository.ConditionRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import com.coinsensor.exchangecoin.service.ExchangeCoinService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinDetectionService {

	private final ConditionRepository conditionRepository;
	private final ExchangeCoinRepository exchangeCoinRepository;
	private final DetectionRepository detectionRepository;
	private final DetectedCoinRepository detectedCoinRepository;
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	private final SimpMessagingTemplate messagingTemplate;
	private final Executor taskExecutor = Executors.newFixedThreadPool(20);
	private final List<String> coinCategories = List.of("all", "top20", "bottom20");
	private final ExchangeCoinService exchangeCoinService;

	@Transactional
	public void detectAbnormalCoins(Condition condition) {
		try {
			Thread.sleep(5000); // 5초 딜레이 - 서버 데이터 갱신 대기
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.warn("탐지 딜레이 중 인터럽트 발생");
		}

		CompletableFuture.runAsync(() ->
			detectBinanceSpotCoins(condition), taskExecutor);
		CompletableFuture.runAsync(() ->
			detectBinanceFutureCoins(condition), taskExecutor);
	}

	private void detectBinanceSpotCoins(Condition condition) {
		List<ExchangeCoin> exchangeCoins = exchangeCoinRepository.findByExchange_NameAndTypeAndIsActive("binance",
			Exchange.Type.spot, true);

		List<CompletableFuture<DetectedCoin>> futures = exchangeCoins.stream()
			.map(exchangeCoin -> CompletableFuture.supplyAsync(() ->
				detectSingleCoin(exchangeCoin, condition, "https://api.binance.com/api/v3/klines"), taskExecutor))
			.toList();

		List<DetectedCoin> detectedCoins = futures.stream()
			.map(CompletableFuture::join)
			.filter(Objects::nonNull)
			.toList();

		if (!detectedCoins.isEmpty()) {
			Exchange exchange = exchangeCoins.getFirst().getExchange();
			BigDecimal changeXAvg = detectedCoins.stream()
				.map(DetectedCoin::getChangeX)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.divide(BigDecimal.valueOf(detectedCoins.size()), 2, RoundingMode.HALF_UP);
			BigDecimal volumeXAvg = detectedCoins.stream()
				.map(DetectedCoin::getVolumeX)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.divide(BigDecimal.valueOf(detectedCoins.size()), 2, RoundingMode.HALF_UP);
			Detection detection = detectionRepository.save(
				Detection.to(condition, exchange, SummaryUtil.create(exchange, condition, detectedCoins),
					(long)detectedCoins.size(), changeXAvg, volumeXAvg));

			detectedCoins = detectedCoins.stream()
				.map(detectedCoin -> detectedCoin.withDetection(detection)).toList();
			detectedCoinRepository.saveAll(detectedCoins);

			processDetectionNotifications(detection, detectedCoins);

			log.info("현물 탐지 완료: {} - {}개 코인", condition.getTimeframe().getName(), detectedCoins.size());
		}
	}

	private void detectBinanceFutureCoins(Condition condition) {
		List<ExchangeCoin> exchangeCoins = exchangeCoinRepository.findByExchange_NameAndTypeAndIsActive("binance",
			Exchange.Type.future, true);

		List<CompletableFuture<DetectedCoin>> futures = exchangeCoins.stream()
			.map(exchangeCoin -> CompletableFuture.supplyAsync(() ->
				detectSingleCoin(exchangeCoin, condition, "https://fapi.binance.com/fapi/v1/klines"), taskExecutor))
			.toList();

		List<DetectedCoin> detectedCoins = futures.stream()
			.map(CompletableFuture::join)
			.filter(Objects::nonNull)
			.toList();

		if (!detectedCoins.isEmpty()) {
			Exchange exchange = exchangeCoins.getFirst().getExchange();
			BigDecimal changeXAvg = detectedCoins.stream()
				.map(DetectedCoin::getChangeX)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.divide(BigDecimal.valueOf(detectedCoins.size()), 2, RoundingMode.HALF_UP);
			BigDecimal volumeXAvg = detectedCoins.stream()
				.map(DetectedCoin::getVolumeX)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.divide(BigDecimal.valueOf(detectedCoins.size()), 2, RoundingMode.HALF_UP);
			Detection detection = detectionRepository.save(
				Detection.to(condition, exchange, SummaryUtil.create(exchange, condition, detectedCoins),
					(long)detectedCoins.size(), changeXAvg, volumeXAvg));

			detectedCoins = detectedCoins.stream()
				.map(detectedCoin -> detectedCoin.withDetection(detection)).toList();
			detectedCoinRepository.saveAll(detectedCoins);

			processDetectionNotifications(detection, detectedCoins);
			log.info("선물 탐지 완료: {} - {}개 코인", condition.getTimeframe().getName(), detectedCoins.size());
		}
	}

	@Transactional
	public void detectByTimeframe(String timeframeName) {
		List<Condition> conditionList = conditionRepository.findAll();

		conditionList.stream()
			.filter(condition -> condition.getTimeframe().getName().equals(timeframeName))
			.forEach(condition -> CompletableFuture.runAsync(() ->
				detectAbnormalCoins(condition), taskExecutor));
	}

	private void processDetectionNotifications(Detection detection, List<DetectedCoin> detectedCoins) {
		for (String coinCategory : coinCategories) {
			String exchangeType = detection.getExchange().getType().name();
			switch (coinCategory) {
				case "top20":
					List<String> top20Tickers = exchangeCoinService.getTopCoins(exchangeType).stream()
						.map(TopBottomCoinResponse::getSymbol)
						.toList();

					List<DetectedCoin> detectedTop20Coin = detectedCoins.stream()
						.filter(coin -> top20Tickers.contains(coin.getExchangeCoin().getCoin().getCoinTicker()))
						.toList();

					if (!detectedTop20Coin.isEmpty()) {
						sendDetectionNotification(detection, detectedTop20Coin, coinCategory);
					}
					break;
				case "bottom20":
					List<String> bottom20Tickers = exchangeCoinService.getBottomCoins(exchangeType).stream()
						.map(TopBottomCoinResponse::getSymbol)
						.toList();
					List<DetectedCoin> detectedBottom20Coin = detectedCoins.stream()
						.filter(coin -> bottom20Tickers.contains(coin.getExchangeCoin().getCoin().getCoinTicker()))
						.toList();
					if (!detectedBottom20Coin.isEmpty()) {
						sendDetectionNotification(detection, detectedBottom20Coin, coinCategory);
					}
					break;
				default:
					sendDetectionNotification(detection, detectedCoins, coinCategory);
					break;
			}
		}
	}

	private void sendDetectionNotification(Detection detection, List<DetectedCoin> detectedCoins, String coinCategory) {
		String timeframe = detection.getCondition().getTimeframe().getName();
		String exchangeName = detection.getExchange().getName();
		String exchangeType = detection.getExchange().getType().name();

		String topic = String.format("/topic/detections?exchange=%s&exchangeType=%s&coinCategory=%s&timeframe=%s",
			exchangeName, exchangeType, coinCategory, timeframe);

		messagingTemplate.convertAndSend(topic, DetectionInfoResponse.of(detection, detectedCoins));
	}

	private DetectedCoin detectSingleCoin(ExchangeCoin exchangeCoin, Condition condition, String baseUrl) {
		Coin coin = exchangeCoin.getCoin();

		try {
			String response = webClient.get()
				.uri(baseUrl + "?symbol=" + coin.getCoinTicker() +
					"&interval=" + condition.getTimeframe().getName() + "&limit=3")
				.retrieve()
				.bodyToMono(String.class)
				.block();

			JsonNode klines = objectMapper.readTree(response);
			if (klines.size() < 3)
				return null;

			JsonNode prevKline = klines.get(0);
			JsonNode currentKline = klines.get(1);

			double prevVolume = prevKline.get(5).asDouble();
			double currentVolume = currentKline.get(5).asDouble();
			double openPrice = currentKline.get(1).asDouble();
			double closePrice = currentKline.get(4).asDouble();
			double lowPrice = currentKline.get(3).asDouble();
			double highPrice = currentKline.get(2).asDouble();

			double priceChangePercent = (highPrice / lowPrice - 1) * 100;
			double volumeRatio = prevVolume > 0 ? currentVolume / prevVolume : 0;

			if (priceChangePercent >= condition.getChangeX().doubleValue() && volumeRatio >= condition.getVolumeX().doubleValue()) {
				if (closePrice < openPrice)
					priceChangePercent *= -1;

				return DetectedCoin.to(exchangeCoin,
					BigDecimal.valueOf(priceChangePercent).setScale(2, RoundingMode.HALF_UP),
					BigDecimal.valueOf(Math.round(volumeRatio * 100.0) / 100.0).setScale(2, RoundingMode.HALF_UP),
					BigDecimal.valueOf(highPrice).setScale(8, RoundingMode.HALF_UP),
					BigDecimal.valueOf(lowPrice).setScale(8, RoundingMode.HALF_UP));
			}

		} catch (Exception e) {
			log.warn("코인 탐지 실패: {}", coin.getCoinTicker());
		}

		return null;
	}
}