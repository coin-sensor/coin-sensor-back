package com.coinsensor.service;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.coin.repository.CoinRepository;
import com.coinsensor.common.util.SummaryUtil;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
import com.coinsensor.detectioncriteria.repository.DetectionCriteriaRepository;
import com.coinsensor.detectiongroup.entity.DetectionGroup;
import com.coinsensor.detectiongroup.repository.DetectionGroupRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinGroupResponse;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinDetectionService {
    
    private final DetectionCriteriaRepository detectionCriteriaRepository;
    private final ExchangeCoinRepository exchangeCoinRepository;
    private final DetectionGroupRepository detectionGroupRepository;
    private final DetectedCoinRepository detectedCoinRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    
    @Transactional
    public void detectAbnormalCoins(DetectionCriteria criteria) {
        detectBinanceSpotCoins(criteria);
        detectBinanceFutureCoins(criteria);
    }
    
    private void detectBinanceSpotCoins(DetectionCriteria criteria) {
        List<DetectedCoin> detectedCoins = new ArrayList<>();
        List<ExchangeCoin> exchangeCoins = exchangeCoinRepository.findByExchange_NameAndTypeAndIsActive("binance", Exchange.Type.spot, true);
        
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
                
                if (priceChangePercent >= criteria.getVolatility().doubleValue() && volumeRatio >= criteria.getVolume()) {
                    if (closePrice < openPrice) priceChangePercent *= -1;

                    DetectedCoin detected = DetectedCoin.builder()
                            .coin(coin)
                            .exchangeCoin(exchangeCoin)
                            .volatility(BigDecimal.valueOf(priceChangePercent).setScale(2, RoundingMode.HALF_UP))
                            .volume(Math.round(volumeRatio * 10.0) / 10.0)
                            .detectedAt(LocalDateTime.now())
                            .build();
                    detectedCoins.add(detected);
                }
                
            } catch (Exception e) {
                log.warn("코인 탐지 실패: {}", coin.getCoinTicker());
            }
        }
        
        if (!detectedCoins.isEmpty()) {
            Exchange exchange = exchangeCoins.getFirst().getExchange();
            DetectionGroup group = DetectionGroup.builder()
                    .detectionCriteria(criteria)
                    .exchange(exchange)
                    .detectedAt(LocalDateTime.now())
                    .detectionCount((long) detectedCoins.size())
                    .summary(SummaryUtil.create(criteria, detectedCoins))
                    .build();
            detectionGroupRepository.save(group);
            
            for (DetectedCoin detected : detectedCoins) {
                detected = DetectedCoin.builder()
                        .detectionGroup(group)
                        .coin(detected.getCoin())
                        .exchangeCoin(detected.getExchangeCoin())
                        .volatility(detected.getVolatility())
                        .volume(detected.getVolume())
                        .detectedAt(detected.getDetectedAt())
                        .build();
                detectedCoinRepository.save(detected);
            }
            
            sendDetectionNotification(group, detectedCoins);
            log.info("현물 탐지 완료: {} - {}개 코인", criteria.getTimeframe().getTimeframeLabel(), detectedCoins.size());
        }
    }
    
    private void detectBinanceFutureCoins(DetectionCriteria criteria) {
        List<DetectedCoin> detectedCoins = new ArrayList<>();
        List<ExchangeCoin> exchangeCoins = exchangeCoinRepository.findByExchange_NameAndTypeAndIsActive("binance", Exchange.Type.future, true);
        
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
                
                if (priceChangePercent >= criteria.getVolatility().doubleValue() && volumeRatio >= criteria.getVolume()) {
                    if (closePrice < openPrice) priceChangePercent *= -1;

                    DetectedCoin detected = DetectedCoin.builder()
                            .coin(coin)
                            .exchangeCoin(exchangeCoin)
                            .volatility(BigDecimal.valueOf(priceChangePercent).setScale(2, RoundingMode.HALF_UP))
                            .volume(Math.round(volumeRatio * 10.0) / 10.0)
                            .detectedAt(LocalDateTime.now())
                            .build();
                    detectedCoins.add(detected);
                }
                
            } catch (Exception e) {
                log.warn("선물 코인 탐지 실패: {}", coin.getCoinTicker());
            }
        }
        
        if (!detectedCoins.isEmpty()) {
            Exchange exchange = exchangeCoins.getFirst().getExchange();
            DetectionGroup group = DetectionGroup.builder()
                    .detectionCriteria(criteria)
                    .exchange(exchange)
                    .detectedAt(LocalDateTime.now())
                    .detectionCount((long) detectedCoins.size())
                    .summary(SummaryUtil.create(criteria, detectedCoins))
                    .build();
            detectionGroupRepository.save(group);
            
            for (DetectedCoin detected : detectedCoins) {
                detected = DetectedCoin.builder()
                        .detectionGroup(group)
                        .coin(detected.getCoin())
                        .exchangeCoin(detected.getExchangeCoin())
                        .volatility(detected.getVolatility())
                        .volume(detected.getVolume())
                        .detectedAt(detected.getDetectedAt())
                        .build();
                detectedCoinRepository.save(detected);
            }
            
            sendDetectionNotification(group, detectedCoins);
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
    
    private void sendDetectionNotification(DetectionGroup group, List<DetectedCoin> detectedCoins) {
        String timeframe = group.getDetectionCriteria().getTimeframe().getTimeframeLabel();
        String exchangeName = group.getExchange().getName();
        String exchangeType = group.getExchange().getType().name();
        
        DetectedCoinGroupResponse response = DetectedCoinGroupResponse.builder()
                .exchangeName(exchangeName)
                .exchangeType(exchangeType)
                .timeframeLabel(timeframe)
                .criteriaVolatility(group.getDetectionCriteria().getVolatility())
                .criteriaVolume(group.getDetectionCriteria().getVolume())
                .detectedAt(group.getDetectedAt())
                .coins(detectedCoins.stream()
                        .map(DetectedCoinResponse::from)
                        .toList())
                .build();
        
        String topic = String.format("/topic/detection/exchanges/%s/exchangeTypes/%s/timeframes/%s",
                exchangeName, exchangeType, timeframe);
        messagingTemplate.convertAndSend(topic, response);
    }
}