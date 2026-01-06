package com.coinsensor.websocket.service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import com.coinsensor.timeframe.entity.Timeframe;
import com.coinsensor.timeframe.repository.TimeframeRepository;
import com.coinsensor.websocket.dto.KlineData;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinanceKlineWebSocketService {

	private final ExchangeCoinRepository exchangeCoinRepository;
	private final TimeframeRepository timeframeRepository;
	private final KlineDetectionService klineDetectionService;
	private final ObjectMapper objectMapper;

	private final ConcurrentMap<String, Disposable> connections = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, AtomicBoolean> reconnecting = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, List<KlineData>> klineBuffer = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, Integer> expectedCoinCount = new ConcurrentHashMap<>();

	public void initWebSocketConnections() {
		log.info("WebSocket 연결 초기화 시작");
		connectToSpotKlines();
		connectToFutureKlines();
	}

	private void connectToSpotKlines() {
		List<ExchangeCoin> spotCoins = exchangeCoinRepository.findByExchangeNameAndTypeAndIsActiveAndEnableDetection(
			"binance", Exchange.Type.spot);

		List<String> timeframes = timeframeRepository.findAll().stream()
			.map(Timeframe::getName)
			.toList();

		for (String timeframe : timeframes) {
			connectToSpotCoinsInBatches(spotCoins, timeframe);
		}
	}

	private void connectToFutureKlines() {
		List<ExchangeCoin> futureCoins = exchangeCoinRepository.findByExchangeNameAndTypeAndIsActiveAndEnableDetection(
			"binance", Exchange.Type.future);

		List<String> timeframes = timeframeRepository.findAll().stream()
			.map(Timeframe::getName)
			.toList();

		for (String timeframe : timeframes) {
			connectToFutureCoinsInBatches(futureCoins, timeframe);
		}
	}

	private void connectToSpotCoinsInBatches(List<ExchangeCoin> coins, String timeframe) {
		if (!coins.isEmpty()) {
			String streamUrl = buildSpotStreamUrl(coins, timeframe);
			String sessionKey = "spot-" + timeframe;
			expectedCoinCount.put(sessionKey, coins.size());
			connectToCombinedStream(streamUrl, sessionKey, false);
		}
	}

	private void connectToFutureCoinsInBatches(List<ExchangeCoin> coins, String timeframe) {
		if (!coins.isEmpty()) {
			String streamUrl = buildFutureStreamUrl(coins, timeframe);
			String sessionKey = "future-" + timeframe;
			expectedCoinCount.put(sessionKey, coins.size());
			connectToCombinedStream(streamUrl, sessionKey, true);
		}
	}

	private String buildSpotStreamUrl(List<ExchangeCoin> coins, String timeframe) {
		StringBuilder streams = new StringBuilder();
		for (ExchangeCoin coin : coins) {
			if (!streams.isEmpty())
				streams.append("/");
			streams.append(coin.getCoin().getCoinTicker().toLowerCase())
				.append("@kline_").append(timeframe);
		}
		return "wss://stream.binance.com:9443/stream?streams=" + streams.toString();
	}

	private String buildFutureStreamUrl(List<ExchangeCoin> coins, String timeframe) {
		StringBuilder streams = new StringBuilder();
		for (ExchangeCoin coin : coins) {
			if (!streams.isEmpty())
				streams.append("/");
			streams.append(coin.getCoin().getCoinTicker().toLowerCase())
				.append("@kline_").append(timeframe);
		}
		return "wss://fstream.binance.com/stream?streams=" + streams;
	}

	private void connectToCombinedStream(String streamUrl, String sessionKey, boolean isFuture) {
		reconnecting.putIfAbsent(sessionKey, new AtomicBoolean(false));
		klineBuffer.put(sessionKey, new java.util.concurrent.CopyOnWriteArrayList<>());

		Disposable connection = HttpClient.create()
			.websocket()
			.uri(streamUrl)
			.handle((inbound, outbound) ->
				inbound.receive()
					.asString()
					.doOnNext(payload -> processKlineMessage(payload, sessionKey, isFuture))
					.doOnError(e -> log.error("{} 메시지 처리 오류: {}", sessionKey, e.getMessage()))
					.then()
			)
			.subscribe(
				null,
				error -> {
					log.warn("{} WebSocket 연결 오류: {}", sessionKey, error.getMessage());
					connections.remove(sessionKey);
					reconnectCombinedStream(streamUrl, sessionKey, isFuture);
				},
				() -> {
					log.info("{} WebSocket 연결 종료", sessionKey);
					connections.remove(sessionKey);
					reconnectCombinedStream(streamUrl, sessionKey, isFuture);
				}
			);

		connections.put(sessionKey, connection);
		log.info("{} WebSocket 연결 성공", sessionKey);
	}

	private void processKlineMessage(String payload, String sessionKey, boolean isFuture) {
		try {
			if (payload.contains("\"e\":\"kline\"")) {
				KlineData klineData = objectMapper.readTree(payload)
					.get("data").traverse(objectMapper).readValueAs(KlineData.class);

				if (klineData.getKline().getIsClosed()) {
					List<KlineData> buffer = klineBuffer.get(sessionKey);
					if (buffer != null) {
						buffer.add(klineData);

						Integer expected = expectedCoinCount.get(sessionKey);
						if (expected != null && buffer.size() >= expected) {
							List<KlineData> toSave = new java.util.ArrayList<>(buffer);
							buffer.clear();
							klineDetectionService.saveOhlcvDataBatch(toSave, isFuture ?
								Exchange.Type.future : Exchange.Type.spot);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("K라인 데이터 처리 오류: {}", e.getMessage());
		}
	}

	private void reconnectCombinedStream(String streamUrl, String sessionKey, boolean isFuture) {
		AtomicBoolean isReconnecting = reconnecting.get(sessionKey);
		if (isReconnecting != null && isReconnecting.compareAndSet(false, true)) {
			Mono.delay(Duration.ofSeconds(5))
				.subscribe(tick -> {
					log.info("{} 재연결 시도...", sessionKey);
					connectToCombinedStream(streamUrl, sessionKey, isFuture);
					isReconnecting.set(false);
				});
		}
	}

	@PreDestroy
	public void closeConnections() {
		connections.values().forEach(connection -> {
			if (connection != null && !connection.isDisposed()) {
				connection.dispose();
			}
		});
		log.info("모든 WebSocket 연결 종료");
	}
}