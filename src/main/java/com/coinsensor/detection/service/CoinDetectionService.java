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
import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
import com.coinsensor.detectioncriteria.repository.DetectionCriteriaRepository;
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

	private final DetectionCriteriaRepository detectionCriteriaRepository;
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
	public void detectAbnormalCoins(DetectionCriteria criteria) {
		try {
			Thread.sleep(5000); // 5초 딜레이 - 서버 데이터 갱신 대기
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.warn("탐지 딜레이 중 인터럽트 발생");
		}

		CompletableFuture.runAsync(() ->
			detectBinanceSpotCoins(criteria), taskExecutor);
		CompletableFuture.runAsync(() ->
			detectBinanceFutureCoins(criteria), taskExecutor);
	}

	private void detectBinanceSpotCoins(DetectionCriteria criteria) {
		List<ExchangeCoin> exchangeCoins = exchangeCoinRepository.findByExchange_NameAndTypeAndIsActive("binance",
			Exchange.Type.spot, true);

		List<CompletableFuture<DetectedCoin>> futures = exchangeCoins.stream()
			.map(exchangeCoin -> CompletableFuture.supplyAsync(() ->
				detectSingleCoin(exchangeCoin, criteria, "https://api.binance.com/api/v3/klines"), taskExecutor))
			.toList();

		List<DetectedCoin> detectedCoins = futures.stream()
			.map(CompletableFuture::join)
			.filter(Objects::nonNull)
			.toList();

		if (!detectedCoins.isEmpty()) {
			Exchange exchange = exchangeCoins.getFirst().getExchange();
			Detection detection = detectionRepository.save(
				Detection.to(criteria, exchange, SummaryUtil.create(criteria, detectedCoins),
					(long)detectedCoins.size()));

			detectedCoins = detectedCoins.stream()
				.map(detectedCoin -> detectedCoin.withDetection(detection)).toList();
			detectedCoinRepository.saveAll(detectedCoins);

			processDetectionNotifications(detection, detectedCoins);

			log.info("현물 탐지 완료: {} - {}개 코인", criteria.getTimeframe().getTimeframeLabel(), detectedCoins.size());
		}
	}

	private void detectBinanceFutureCoins(DetectionCriteria criteria) {
		List<ExchangeCoin> exchangeCoins = exchangeCoinRepository.findByExchange_NameAndTypeAndIsActive("binance",
			Exchange.Type.future, true);

		List<CompletableFuture<DetectedCoin>> futures = exchangeCoins.stream()
			.map(exchangeCoin -> CompletableFuture.supplyAsync(() ->
				detectSingleCoin(exchangeCoin, criteria, "https://fapi.binance.com/fapi/v1/klines"), taskExecutor))
			.toList();

		List<DetectedCoin> detectedCoins = futures.stream()
			.map(CompletableFuture::join)
			.filter(Objects::nonNull)
			.toList();

		if (!detectedCoins.isEmpty()) {
			Exchange exchange = exchangeCoins.getFirst().getExchange();

			Detection detection = detectionRepository.save(
				Detection.to(criteria, exchange, SummaryUtil.create(criteria, detectedCoins),
					(long)detectedCoins.size()));

			detectedCoins = detectedCoins.stream()
				.map(detectedCoin -> detectedCoin.withDetection(detection)).toList();
			detectedCoinRepository.saveAll(detectedCoins);

			processDetectionNotifications(detection, detectedCoins);
			log.info("선물 탐지 완료: {} - {}개 코인", criteria.getTimeframe().getTimeframeLabel(), detectedCoins.size());
		}
	}

	@Transactional
	public void detectByTimeframe(String timeframeLabel) {
		List<DetectionCriteria> criteriaList = detectionCriteriaRepository.findAll();

		criteriaList.stream()
			.filter(criteria -> criteria.getTimeframe().getTimeframeLabel().equals(timeframeLabel))
			.forEach(criteria -> CompletableFuture.runAsync(() ->
				detectAbnormalCoins(criteria), taskExecutor));
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
		String timeframe = detection.getDetectionCriteria().getTimeframe().getTimeframeLabel();
		String exchangeName = detection.getExchange().getName();
		String exchangeType = detection.getExchange().getType().name();

		String topic = String.format("/topic/detections?exchanges=%s&exchangeTypes=%s&coinCategory=%s&timeframes=%s",
			exchangeName, exchangeType, coinCategory, timeframe);

		messagingTemplate.convertAndSend(topic, DetectionInfoResponse.of(detection, detectedCoins));
	}

	private DetectedCoin detectSingleCoin(ExchangeCoin exchangeCoin, DetectionCriteria criteria, String baseUrl) {
		Coin coin = exchangeCoin.getCoin();

		try {
			String response = webClient.get()
				.uri(baseUrl + "?symbol=" + coin.getCoinTicker() +
					"&interval=" + criteria.getTimeframe().getTimeframeLabel() + "&limit=3")
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

			if (priceChangePercent >= criteria.getVolatility().doubleValue() && volumeRatio >= criteria.getVolume()) {
				if (closePrice < openPrice)
					priceChangePercent *= -1;

				return DetectedCoin.to(exchangeCoin,
					BigDecimal.valueOf(priceChangePercent).setScale(2, RoundingMode.HALF_UP),
					Math.round(volumeRatio * 10.0) / 10.0, highPrice, lowPrice);
			}

		} catch (Exception e) {
			log.warn("코인 탐지 실패: {}", coin.getCoinTicker());
		}

		return null;
	}
}