package com.coinsensor.detectedcoin.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinGroupResponse;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detectiongroup.repository.DetectionGroupRepository;
import com.coinsensor.exchange.entity.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DetectedCoinServiceImpl implements DetectedCoinService {
    
    private final DetectedCoinRepository detectedCoinRepository;
    private final DetectionGroupRepository detectionGroupRepository;
    
    @Override
    public List<DetectedCoinResponse> getAbnormalCoins() {
        return detectedCoinRepository.findAll().stream()
                .map(DetectedCoinResponse::from)
                .toList();
    }
    
    @Override
    public List<DetectedCoinResponse> getVolatileCoins() {
        return detectedCoinRepository.findAll().stream()
                .sorted((a, b) -> b.getVolatility().compareTo(a.getVolatility()))
                .limit(20)
                .map(DetectedCoinResponse::from)
                .toList();
    }
    
    @Override
    public List<DetectedCoinResponse> getDetectedCoinsByTimeAndType(String exchangeName, String exchangeType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusMinutes(1);
        
        Exchange.ExchangeType type = Exchange.ExchangeType.valueOf(exchangeType);
        return detectedCoinRepository.findByExchangeNameAndTypeAndTime(exchangeName, type, startTime, endTime)
                .stream()
                .map(DetectedCoinResponse::from)
                .toList();
    }
    
    @Override
    public DetectedCoinGroupResponse getDetectedCoinGroupByTimeAndType(String exchangeName, String timeframeLabel, String exchangeType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusMinutes(1);
        
        Exchange.ExchangeType type = Exchange.ExchangeType.valueOf(exchangeType);
        return detectionGroupRepository.findByExchangeAndTimeframeAndTime(
                exchangeName, type, timeframeLabel, startTime, endTime)
                .map(group -> {
                    List<DetectedCoin> detectedCoins = detectedCoinRepository.findByDetectionGroup_DetectionGroupId(group.getDetectionGroupId());
                    return DetectedCoinGroupResponse.builder()
                            .exchangeName(group.getExchange().getName())
                            .exchangeType(group.getExchange().getExchangeType().name())
                            .timeframeLabel(group.getDetectionCriteria().getTimeframe().getTimeframeLabel())
                            .criteriaVolatility(group.getDetectionCriteria().getVolatility())
                            .criteriaVolume(group.getDetectionCriteria().getVolume())
                            .detectedAt(group.getDetectedAt())
                            .coins(detectedCoins.stream().map(DetectedCoinResponse::from).toList())
                            .build();
                })
                .orElse(null);
    }
}
