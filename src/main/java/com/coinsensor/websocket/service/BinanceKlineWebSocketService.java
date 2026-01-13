package com.coinsensor.websocket.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Service;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.service.ExchangeCoinService;
import com.coinsensor.ohlcvs.service.OhlcvService;
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

	private final TimeframeRepository timeframeRepository;
	private final ExchangeCoinService exchangeCoinService;
	private final OhlcvService ohlcvService;
	private final ObjectMapper objectMapper;

	private final ConcurrentMap<String, Disposable> connections = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, AtomicBoolean> reconnecting = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, List<KlineData>> klineBuffer = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, Integer> expectedCoinCount = new ConcurrentHashMap<>();

	// TODO: 바이낸스 현물 웹소켓 상태이상으로 임시 비활성화
	public void initWebSocketConnections() {
		log.info("WebSocket 연결 초기화 시작");
		connectToSpotKlines();
		connectToFutureKlines();
	}

	private void connectToSpotKlines() {
		List<ExchangeCoin> spotCoins = exchangeCoinService.getDetectableExchangeCoins(
			"binance", Exchange.Type.spot);

		List<String> timeframes = timeframeRepository.findAll().stream()
			.map(Timeframe::getName)
			.toList();

		for (String timeframe : timeframes) {
			connectToSpotCoins(spotCoins, timeframe);
		}
	}

	private void connectToFutureKlines() {
		List<ExchangeCoin> futureCoins = exchangeCoinService.getDetectableExchangeCoins(
			"binance", Exchange.Type.future);

		List<String> timeframes = timeframeRepository.findAll().stream()
			.map(Timeframe::getName)
			.toList();

		for (String timeframe : timeframes) {
			connectToFutureCoins(futureCoins, timeframe);
		}
	}

	private void connectToSpotCoins(List<ExchangeCoin> coins, String timeframe) {
		if (!coins.isEmpty()) {
			String streamUrl = buildSpotStreamUrl(coins, timeframe);
			String sessionKey = "spot-" + timeframe;
			expectedCoinCount.put(sessionKey, coins.size());
			connectToCombinedStream(streamUrl, sessionKey, false);
		}
	}

	private void connectToFutureCoins(List<ExchangeCoin> coins, String timeframe) {
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
		return "wss://stream.binance.com:9443/stream?streams=" + streams;
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
					log.warn("[binance-{}] WebSocket 연결 오류: {}", sessionKey, error.getMessage());
					connections.remove(sessionKey);
					reconnectCombinedStream(streamUrl, sessionKey, isFuture);
				},
				() -> {
					log.info("[binance-{}] WebSocket 연결 종료", sessionKey);
					connections.remove(sessionKey);
					reconnectCombinedStream(streamUrl, sessionKey, isFuture);
				}
			);

		connections.put(sessionKey, connection);
		log.info("[binance-{}] WebSocket 연결 성공", sessionKey);
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
							List<KlineData> toSave = new ArrayList<>(buffer);
							buffer.clear();
							ohlcvService.saveKlineData(toSave,
								isFuture ? Exchange.Type.future : Exchange.Type.spot);
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