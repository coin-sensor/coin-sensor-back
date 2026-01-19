package com.coinsensor.scheduler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.coin.repository.CoinRepository;
import com.coinsensor.common.annotation.LeaderOnly;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchange.repository.ExchangeRepository;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinanceCoinScheduler {
	private static final String BINANCE_SPOT = "binance-spot";
	private static final String BINANCE_FUTURE = "binance-future";
	private final CoinRepository coinRepository;
	private final ExchangeCoinRepository exchangeCoinRepository;
	private final ExchangeRepository exchangeRepository;
	private final WebClient webClient;
	private final ObjectMapper objectMapper;
	private final Set<String> disabledDetectionTickers = Set.of("FLOWUSDT", "BTTCUSDT", "1000SATSUSDT", "ALICEUSDT",
		"DYDXUSDT", "GTCUSDT", "CELOUSDT", "C98USDT", "DENTUSDT", "币安人生USDT");

	@LeaderOnly
	@Scheduled(cron = "0 1 * * * *")
	@Transactional
	public void syncBinanceCoins() {
		syncSpotCoins();
		syncFuturesCoins();
	}

	private void syncSpotCoins() {
		Exchange binanceSpot = exchangeRepository.findByNameAndType("binance", Exchange.Type.spot)
			.orElseGet(() -> exchangeRepository.save(Exchange.builder()
				.name("binance")
				.type(Exchange.Type.spot)
				.build()));

		try {
			String response = webClient.get()
				.uri("https://api.binance.com/api/v3/exchangeInfo")
				.retrieve()
				.bodyToMono(String.class)
				.block();

			JsonNode root = objectMapper.readTree(response);
			JsonNode symbols = root.get("symbols");

			Set<String> activeTickers = new HashSet<>();
			int newCoins = 0;

			for (JsonNode symbol : symbols) {
				String status = symbol.get("status").asText();
				if (!"TRADING".equals(status))
					continue;

				String coinTicker = symbol.get("symbol").asText();
				String baseAsset = symbol.get("baseAsset").asText();
				String quoteAsset = symbol.get("quoteAsset").asText();

				if (!"USDT".equals(quoteAsset))
					continue;

				activeTickers.add(coinTicker);

				if (!exchangeCoinRepository.existsByExchange_ExchangeIdAndCoin_CoinTicker(
					binanceSpot.getExchangeId(), coinTicker)) {

					Coin coin = coinRepository.findByCoinTicker(coinTicker)
						.orElseGet(() -> coinRepository.save(Coin.builder()
							.coinTicker(coinTicker)
							.baseAsset(baseAsset)
							.build()));

					ExchangeCoin exchangeCoin = ExchangeCoin.builder()
						.exchange(binanceSpot)
						.coin(coin)
						.isActive(true)
						.enableDetection(!disabledDetectionTickers.contains(coinTicker))
						.build();
					exchangeCoinRepository.save(exchangeCoin);
					newCoins++;
				}
			}

			// 상폐된 코인 비활성화
			int deactivated = exchangeCoinRepository
				.findByExchange_ExchangeId(binanceSpot.getExchangeId())
				.stream()
				.filter(ec -> ec.getIsActive() && !activeTickers.contains(ec.getCoin().getCoinTicker()))
				.peek(ec -> {
					ec.setIsActive(false);
					exchangeCoinRepository.save(ec);
				})
				.toList()
				.size();

			log.info("[{}] 코인 정보 동기화 완료: {} 개 신규 추가, {} 개 상폐", BINANCE_SPOT, newCoins, deactivated);

		} catch (Exception e) {
			log.error("[{}] 코인 정보 동기화 실패", BINANCE_SPOT, e);
		}
	}

	private void syncFuturesCoins() {
		Exchange binanceFuture = exchangeRepository.findByNameAndType("binance", Exchange.Type.future)
			.orElseGet(() -> exchangeRepository.save(Exchange.builder()
				.name("binance")
				.type(Exchange.Type.future)
				.build()));

		try {
			String response = webClient.get()
				.uri("https://fapi.binance.com/fapi/v1/exchangeInfo")
				.retrieve()
				.bodyToMono(String.class)
				.block();

			JsonNode root = objectMapper.readTree(response);
			JsonNode symbols = root.get("symbols");

			Set<String> activeTickers = new HashSet<>();
			int newCoins = 0;

			for (JsonNode symbol : symbols) {
				String status = symbol.get("status").asText();
				if (!"TRADING".equals(status))
					continue;

				String contractType = symbol.get("contractType").asText();
				if (!"PERPETUAL".equals(contractType))
					continue;

				String coinTicker = symbol.get("symbol").asText();
				String baseAsset = symbol.get("baseAsset").asText();
				String quoteAsset = symbol.get("quoteAsset").asText();

				if (!"USDT".equals(quoteAsset))
					continue;

				activeTickers.add(coinTicker);

				if (!exchangeCoinRepository.existsByExchange_ExchangeIdAndCoin_CoinTicker(
					binanceFuture.getExchangeId(), coinTicker)) {

					Coin coin = coinRepository.findByCoinTicker(coinTicker)
						.orElseGet(() -> coinRepository.save(Coin.builder()
							.coinTicker(coinTicker)
							.baseAsset(baseAsset)
							.build()));

					ExchangeCoin exchangeCoin = ExchangeCoin.builder()
						.exchange(binanceFuture)
						.coin(coin)
						.isActive(true)
						.enableDetection(!disabledDetectionTickers.contains(coinTicker))
						.build();
					exchangeCoinRepository.save(exchangeCoin);
					newCoins++;
				}
			}

			int deactivated = exchangeCoinRepository
				.findByExchange_ExchangeId(binanceFuture.getExchangeId())
				.stream()
				.filter(ec -> ec.getIsActive() && !activeTickers.contains(ec.getCoin().getCoinTicker()))
				.peek(ec -> {
					ec.setIsActive(false);
					exchangeCoinRepository.save(ec);
				})
				.toList()
				.size();

			log.info("[{}] 코인 정보 동기화 완료: {} 개 신규 추가, {} 개 상폐", BINANCE_FUTURE, newCoins, deactivated);

		} catch (Exception e) {
			log.error("[{}] 코인 정보 동기화 실패", BINANCE_FUTURE, e);
		}
	}
}
