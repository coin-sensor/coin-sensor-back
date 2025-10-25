package com.coinsensor.service;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.coin.repository.CoinRepository;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
import com.coinsensor.detectioncriteria.repository.DetectionCriteriaRepository;
import com.coinsensor.detectiongroup.entity.DetectionGroup;
import com.coinsensor.detectiongroup.repository.DetectionGroupRepository;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinDetectionService {
    
    private final DetectionCriteriaRepository detectionCriteriaRepository;
    private final CoinRepository coinRepository;
    private final DetectionGroupRepository detectionGroupRepository;
    private final DetectedCoinRepository detectedCoinRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public void detectAbnormalCoins(DetectionCriteria criteria) {
        detectSpotCoins(criteria);
        detectFutureCoins(criteria);
    }
    
    private void detectSpotCoins(DetectionCriteria criteria) {
        List<DetectedCoin> detectedCoins = new ArrayList<>();
        List<ExchangeCoin> exchangeCoins = coinRepository.findSpotExchangeCoins();
        
        for (ExchangeCoin exchangeCoin : exchangeCoins) {
            Coin coin = exchangeCoin.getCoin();
            
            try {
                String response = webClient.get()
                        .uri("https://api.binance.com/api/v3/klines?symbol=" + coin.getCoinTicker() + 
                             "&interval=" + criteria.getTimeframe().getTimeframeLabel() + "&limit=3")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                
                JsonNode klines = objectMapper.readTree(response);
                if (klines.size() < 3) continue;
                
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
                
                if (priceChangePercent >= criteria.getVolatility() && volumeRatio >= criteria.getVolume()) {
                    if (closePrice < openPrice) priceChangePercent *= -1;

                    DetectedCoin detected = DetectedCoin.builder()
                            .coin(coin)
                            .exchangeCoin(exchangeCoin)
                            .volatility(Math.round(priceChangePercent * 10.0) / 10.0)
                            .volume(Math.round(volumeRatio * 10.0) / 10.0)
                            .createdAt(LocalDateTime.now())
                            .build();
                    detectedCoins.add(detected);
                }
                
            } catch (Exception e) {
                log.warn("코인 탐지 실패: {}", coin.getCoinTicker());
            }
        }
        
        if (!detectedCoins.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(EEE) HH시 mm분 ss초", Locale.KOREAN);
            String timestamp = now.format(formatter);
            
            StringBuilder summary = new StringBuilder();
            summary.append(String.format("🚨 %s 🚨\n\n", timestamp));
            summary.append(String.format("기준 : %s (현물), 기준 변동률 : %.2f%%, 기준 배수 : %.1f배\n\n",
                    criteria.getTimeframe().getTimeframeLabel(),
                    criteria.getVolatility(),
                    criteria.getVolume()));
            
            for (DetectedCoin detected : detectedCoins) {
                summary.append(String.format("종목 : %s\n변동률 : %5.2f%%,  거래량 : %5.1f배\n\n",
                        detected.getCoin().getCoinTicker(),
                        detected.getVolatility(),
                        detected.getVolume()));
            }
            
            DetectionGroup group = DetectionGroup.builder()
                    .detectionCriteria(criteria)
                    .detectedAt(LocalDateTime.now())
                    .detectionCount((long) detectedCoins.size())
                    .summary(summary.toString())
                    .build();
            detectionGroupRepository.save(group);
            
            for (DetectedCoin detected : detectedCoins) {
                detected = DetectedCoin.builder()
                        .detectionGroup(group)
                        .coin(detected.getCoin())
                        .exchangeCoin(detected.getExchangeCoin())
                        .volatility(detected.getVolatility())
                        .volume(detected.getVolume())
                        .createdAt(detected.getCreatedAt())
                        .build();
                detectedCoinRepository.save(detected);
            }
            
            log.info("현물 탐지 완료: {} - {}개 코인", criteria.getTimeframe().getTimeframeLabel(), detectedCoins.size());
        }
    }
    
    private void detectFutureCoins(DetectionCriteria criteria) {
        List<DetectedCoin> detectedCoins = new ArrayList<>();
        List<ExchangeCoin> exchangeCoins = coinRepository.findFutureExchangeCoins();
        
        for (ExchangeCoin exchangeCoin : exchangeCoins) {
            Coin coin = exchangeCoin.getCoin();
            
            try {
                String response = webClient.get()
                        .uri("https://fapi.binance.com/fapi/v1/klines?symbol=" + coin.getCoinTicker() + 
                             "&interval=" + criteria.getTimeframe().getTimeframeLabel() + "&limit=3")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                
                JsonNode klines = objectMapper.readTree(response);
                if (klines.size() < 3) continue;
                
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
                
                if (priceChangePercent >= criteria.getVolatility() && volumeRatio >= criteria.getVolume()) {
                    if (closePrice < openPrice) priceChangePercent *= -1;

                    DetectedCoin detected = DetectedCoin.builder()
                            .coin(coin)
                            .exchangeCoin(exchangeCoin)
                            .volatility(Math.round(priceChangePercent * 10.0) / 10.0)
                            .volume(Math.round(volumeRatio * 10.0) / 10.0)
                            .createdAt(LocalDateTime.now())
                            .build();
                    detectedCoins.add(detected);
                }
                
            } catch (Exception e) {
                log.warn("선물 코인 탐지 실패: {}", coin.getCoinTicker());
            }
        }
        
        if (!detectedCoins.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(EEE) HH시 mm분 ss초", Locale.KOREAN);
            String timestamp = now.format(formatter);
            
            StringBuilder summary = new StringBuilder();
            summary.append(String.format("🚨 %s 🚨\n\n", timestamp));
            summary.append(String.format("기준 : %s (선물), 기준 변동률 : %.2f%%, 기준 배수 : %.1f배\n\n",
                    criteria.getTimeframe().getTimeframeLabel(),
                    criteria.getVolatility(),
                    criteria.getVolume()));
            
            for (DetectedCoin detected : detectedCoins) {
                summary.append(String.format("종목 : %s\n변동률 : %5.2f%%,  거래량 : %5.1f배\n\n",
                        detected.getCoin().getCoinTicker(),
                        detected.getVolatility(),
                        detected.getVolume()));
            }
            
            DetectionGroup group = DetectionGroup.builder()
                    .detectionCriteria(criteria)
                    .detectedAt(LocalDateTime.now())
                    .detectionCount((long) detectedCoins.size())
                    .summary(summary.toString())
                    .build();
            detectionGroupRepository.save(group);
            
            for (DetectedCoin detected : detectedCoins) {
                detected = DetectedCoin.builder()
                        .detectionGroup(group)
                        .coin(detected.getCoin())
                        .exchangeCoin(detected.getExchangeCoin())
                        .volatility(detected.getVolatility())
                        .volume(detected.getVolume())
                        .createdAt(detected.getCreatedAt())
                        .build();
                detectedCoinRepository.save(detected);
            }
            
            log.info("선물 탐지 완료: {} - {}개 코인", criteria.getTimeframe().getTimeframeLabel(), detectedCoins.size());
        }
    }
    
    @Transactional
    public void detectByTimeframe(String timeframeLabel) {
        List<DetectionCriteria> criteriaList = detectionCriteriaRepository.findAll();
        for (DetectionCriteria criteria : criteriaList) {
            if (criteria.getTimeframe().getTimeframeLabel().equals(timeframeLabel)) {
                detectAbnormalCoins(criteria);
            }
        }
    }
}