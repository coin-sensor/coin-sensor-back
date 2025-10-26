package com.coinsensor.scheduler;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.coin.repository.CoinRepository;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchange.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class BinanceCoinScheduler {
    
    private final CoinRepository coinRepository;
    private final ExchangeCoinRepository exchangeCoinRepository;
    private final ExchangeRepository exchangeRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void syncBinanceCoins() {
        syncSpotCoins();
        syncFuturesCoins();
    }
    
    private void syncSpotCoins() {
        log.info("바이낸스 현물 코인 정보 동기화 시작");
        
        Exchange binanceSpot = exchangeRepository.findByNameAndExchangeType("binance", Exchange.ExchangeType.spot)
                .orElseGet(() -> exchangeRepository.save(Exchange.builder()
                        .name("binance")
                        .exchangeType(Exchange.ExchangeType.spot)
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
                if (!"TRADING".equals(status)) continue;
                
                String coinTicker = symbol.get("symbol").asText();
                String baseAsset = symbol.get("baseAsset").asText();
                String quoteAsset = symbol.get("quoteAsset").asText();
                
                if (!"USDT".equals(quoteAsset)) continue;
                
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
            
            log.info("바이낸스 현물 코인 정보 동기화 완료: {} 개 신규 추가, {} 개 상폐", newCoins, deactivated);
            
        } catch (Exception e) {
            log.error("바이낸스 현물 코인 정보 동기화 실패", e);
        }
    }
    
    private void syncFuturesCoins() {
        log.info("바이낸스 선물 코인 정보 동기화 시작");
        
        Exchange binanceFuture = exchangeRepository.findByNameAndExchangeType("binance", Exchange.ExchangeType.future)
                .orElseGet(() -> exchangeRepository.save(Exchange.builder()
                        .name("binance")
                        .exchangeType(Exchange.ExchangeType.future)
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
                if (!"TRADING".equals(status)) continue;
                
                String contractType = symbol.get("contractType").asText();
                if (!"PERPETUAL".equals(contractType)) continue;
                
                String coinTicker = symbol.get("symbol").asText();
                String baseAsset = symbol.get("baseAsset").asText();
                String quoteAsset = symbol.get("quoteAsset").asText();
                
                if (!"USDT".equals(quoteAsset)) continue;
                
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
            
            log.info("바이낸스 선물 코인 정보 동기화 완료: {} 개 신규 추가, {} 개 상폐", newCoins, deactivated);
            
        } catch (Exception e) {
            log.error("바이낸스 선물 코인 정보 동기화 실패", e);
        }
    }
}
