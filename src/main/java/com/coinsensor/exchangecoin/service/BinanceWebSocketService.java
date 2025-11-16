package com.coinsensor.exchangecoin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Disposable;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinanceWebSocketService {

	private final ObjectMapper objectMapper;

	private List<TopBottomCoinResponse> spotTopCoins = Collections.synchronizedList(new ArrayList<>());
	private List<TopBottomCoinResponse> spotBottomCoins = Collections.synchronizedList(new ArrayList<>());
	private List<TopBottomCoinResponse> futuresTopCoins = Collections.synchronizedList(new ArrayList<>());
	private List<TopBottomCoinResponse> futuresBottomCoins = Collections.synchronizedList(new ArrayList<>());

	private Disposable spotConnection;
	private Disposable futuresConnection;

	@PostConstruct
	public void connect() {
		connectToSpotWebSocket();
		connectToFuturesWebSocket();
	}

	private void connectToSpotWebSocket() {
		spotConnection = HttpClient.create()
			.websocket()
			.uri("wss://stream.binance.com:9443/ws/!ticker@arr")
			.handle((inbound, outbound) -> {
				inbound.withConnection(conn -> {
					conn.channel().pipeline().forEach(entry -> {
						if (entry.getValue() instanceof io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder) {
							String name = entry.getKey();
							conn.channel().pipeline().replace(name, name,
								new io.netty.handler.codec.http.websocketx.WebSocket08FrameDecoder(false, false,
									1024 * 1024, false));
						}
					});
				});
				return inbound.receive()
					.asString()
					.doOnNext(this::processSpotTickerData)
					.doOnError(e -> log.warn("현물 메시지 처리 오류", e))
					.then();
			})
			.subscribe(
				null,
				error -> {
					log.error("현물 WebSocket 연결 오류, 재연결 시도", error);
					reconnectSpot();
				},
				() -> log.info("현물 WebSocket 연결 종료")
			);

		log.info("Binance Spot WebSocket 연결 완료");
	}

	private void connectToFuturesWebSocket() {
		futuresConnection = HttpClient.create()
			.websocket()
			.uri("wss://fstream.binance.com/ws/!ticker@arr")
			.handle((inbound, outbound) -> {
				// WebSocket 연결 후 파이프라인 수정
				inbound.withConnection(conn -> {
					conn.channel().pipeline().forEach(entry -> {
						if (entry.getValue() instanceof io.netty.handler.codec.http.websocketx.WebSocketFrameDecoder) {
							String name = entry.getKey();
							conn.channel().pipeline().replace(name, name,
								new io.netty.handler.codec.http.websocketx.WebSocket08FrameDecoder(false, false,
									1024 * 1024, false));
						}
					});
				});
				return inbound.receive()
					.asString()
					.doOnNext(this::processFuturesTickerData)
					.doOnError(e -> log.warn("선물 메시지 처리 오류", e))
					.then();
			})
			.subscribe(
				null,
				error -> {
					log.error("WebSocket 연결 오류, 재연결 시도", error);
					reconnect();
				},
				() -> log.info("WebSocket 연결 종료")
			);

		log.info("Binance Futures WebSocket 연결 완료");
	}

	private void processFuturesTickerData(String message) {
		try {
			JsonNode tickers = objectMapper.readTree(message);

			List<TopBottomCoinResponse> allCoins = new ArrayList<>();
			for (JsonNode ticker : tickers) {
				String symbol = ticker.get("s").asText();
				if (symbol.endsWith("USDT")) {
					allCoins.add(TopBottomCoinResponse.builder()
						.symbol(symbol)
						.priceChangePercent(ticker.get("P").asDouble())
						.build());
				}
			}

			allCoins.sort(Comparator.comparing(TopBottomCoinResponse::getPriceChangePercent).reversed());

			futuresTopCoins = allCoins.stream().limit(20).toList();
			futuresBottomCoins = allCoins.stream().skip(Math.max(0, allCoins.size() - 20)).toList();

		} catch (Exception e) {
			log.warn("티커 데이터 처리 실패", e);
		}
	}

	private void reconnect() {
		try {
			Thread.sleep(5000);
			connectToFuturesWebSocket();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void reconnectSpot() {
		try {
			Thread.sleep(5000);
			connectToSpotWebSocket();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private void processSpotTickerData(String message) {
		try {
			JsonNode tickers = objectMapper.readTree(message);

			List<TopBottomCoinResponse> allCoins = new ArrayList<>();
			for (JsonNode ticker : tickers) {
				String symbol = ticker.get("s").asText();
				if (symbol.endsWith("USDT")) {
					allCoins.add(TopBottomCoinResponse.builder()
						.symbol(symbol)
						.priceChangePercent(ticker.get("P").asDouble())
						.build());
				}
			}

			allCoins.sort(Comparator.comparing(TopBottomCoinResponse::getPriceChangePercent).reversed());

			spotTopCoins = allCoins.stream().limit(20).toList();
			spotBottomCoins = allCoins.stream().skip(Math.max(0, allCoins.size() - 20)).toList();

		} catch (Exception e) {
			log.warn("현물 티커 데이터 처리 실패", e);
		}
	}

	public List<TopBottomCoinResponse> getFuturesTopCoins() {
		return new ArrayList<>(futuresTopCoins);
	}

	public List<TopBottomCoinResponse> getFuturesBottomCoins() {
		return new ArrayList<>(futuresBottomCoins);
	}

	public List<TopBottomCoinResponse> getSpotTopCoins() {
		return new ArrayList<>(spotTopCoins);
	}

	public List<TopBottomCoinResponse> getSpotBottomCoins() {
		return new ArrayList<>(spotBottomCoins);
	}

	@PreDestroy
	public void disconnect() {
		if (futuresConnection != null && !futuresConnection.isDisposed()) {
			futuresConnection.dispose();
		}
		if (spotConnection != null && !spotConnection.isDisposed()) {
			spotConnection.dispose();
		}
		log.info("Binance WebSocket 연결 종료");
	}
}
