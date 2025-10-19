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
    
    @Scheduled(cron = "0 0 * * * *") // 매 정시(0분 0초)마다 실행
    public void syncBinanceCoins() {
        log.info("바이낸스 현물 코인 정보 동기화 시작");
        
        Exchange binance = exchangeRepository.findByName("binance")
                .orElseGet(() -> exchangeRepository.save(Exchange.builder()
                        .name("binance")
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
                
                activeTickers.add(coinTicker);
                
                if (!exchangeCoinRepository.existsByExchange_ExchangeIdAndCoin_CoinTickerAndExchangeType(
                        binance.getExchangeId(), coinTicker, ExchangeCoin.ExchangeType.spot)) {
                    
                    Coin coin = coinRepository.findByCoinTicker(coinTicker)
                            .orElseGet(() -> coinRepository.save(Coin.builder()
                                    .coinTicker(coinTicker)
                                    .baseAsset(baseAsset)
                                    .build()));
                    
                    ExchangeCoin exchangeCoin = ExchangeCoin.builder()
                            .exchange(binance)
                            .coin(coin)
                            .isActive(true)
                            .exchangeType(ExchangeCoin.ExchangeType.spot)
                            .build();
                    exchangeCoinRepository.save(exchangeCoin);
                    newCoins++;
                }
            }
            
            // 상폐된 코인 비활성화
            int deactivated = exchangeCoinRepository
                    .findByExchange_ExchangeIdAndExchangeType(binance.getExchangeId(), ExchangeCoin.ExchangeType.spot)
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
            log.error("바이낸스 코인 정보 동기화 실패", e);
        }
    }
}
