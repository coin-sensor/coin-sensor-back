package com.coinsensor.detectedcoin.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinGroupResponse;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.exchangecoin.entity.ExchangeCoin.ExchangeType;
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
    public List<DetectedCoinResponse> getDetectedCoinsByTimeAndType(ExchangeType exchangeType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusMinutes(1);
        
        return detectedCoinRepository.findByDetectionTimeRangeAndExchangeType(startTime, endTime, exchangeType)
                .stream()
                .map(DetectedCoinResponse::from)
                .toList();
    }
    
    @Override
    public DetectedCoinGroupResponse getDetectedCoinGroupByTimeAndType(String timeframeLabel, ExchangeType exchangeType) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusMinutes(1);
        
        List<DetectedCoin> detectedCoins = detectedCoinRepository.findByTimeframeAndExchangeType(startTime, endTime, timeframeLabel, exchangeType);
        
        if (detectedCoins.isEmpty()) {
            return null;
        }
        
        DetectedCoin firstCoin = detectedCoins.get(0);
        return DetectedCoinGroupResponse.builder()
                .timeframeLabel(firstCoin.getDetectionGroup().getDetectionCriteria().getTimeframe().getTimeframeLabel())
                .criteriaVolatility(firstCoin.getDetectionGroup().getDetectionCriteria().getVolatility())
                .criteriaVolume(firstCoin.getDetectionGroup().getDetectionCriteria().getVolume())
                .detectedAt(firstCoin.getDetectionGroup().getDetectedAt())
                .coins(detectedCoins.stream().map(DetectedCoinResponse::from).toList())
                .build();
    }
}
