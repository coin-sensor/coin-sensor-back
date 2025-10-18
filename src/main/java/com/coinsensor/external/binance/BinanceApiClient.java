package com.coinsensor.external.binance;

import com.coinsensor.dto.CoinDataDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinanceApiClient {
    
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    private static final String BINANCE_API_URL = "https://api.binance.com";
    private static final String TICKER_24HR_ENDPOINT = "/api/v3/ticker/24hr";
    
    public Mono<List<CoinDataDto>> get24hrTickers() {
        return webClient.get()
                .uri(BINANCE_API_URL + TICKER_24HR_ENDPOINT)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseTickers)
                .doOnError(error -> log.error("Failed to fetch 24hr tickers from Binance", error));
    }
    
    public Mono<CoinDataDto> getSymbolTicker(String symbol) {
        return webClient.get()
                .uri(BINANCE_API_URL + TICKER_24HR_ENDPOINT + "?symbol=" + symbol)
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseSingleTicker)
                .doOnError(error -> log.error("Failed to fetch ticker for symbol: {}", symbol, error));
    }
    
    private List<CoinDataDto> parseTickers(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            return StreamSupport.stream(rootNode.spliterator(), false)
                    .map(this::parseTickerNode)
                    .filter(ticker -> ticker.getSymbol().endsWith("USDT"))
                    .toList();
        } catch (Exception e) {
            log.error("Failed to parse tickers response", e);
            return List.of();
        }
    }
    
    private CoinDataDto parseSingleTicker(String jsonResponse) {
        try {
            JsonNode node = objectMapper.readTree(jsonResponse);
            return parseTickerNode(node);
        } catch (Exception e) {
            log.error("Failed to parse single ticker response", e);
            return new CoinDataDto();
        }
    }
    
    private CoinDataDto parseTickerNode(JsonNode node) {
        CoinDataDto ticker = new CoinDataDto();
        ticker.setSymbol(node.get("symbol").asText());
        ticker.setCurrentPrice(new BigDecimal(node.get("lastPrice").asText()));
        ticker.setVolume24h(new BigDecimal(node.get("volume").asText()));
        ticker.setPriceChangePercent24h(new BigDecimal(node.get("priceChangePercent").asText()));
        ticker.setHigh24h(new BigDecimal(node.get("highPrice").asText()));
        ticker.setLow24h(new BigDecimal(node.get("lowPrice").asText()));
        ticker.setLastUpdated(LocalDateTime.now());
        return ticker;
    }
}