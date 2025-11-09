package com.coinsensor.detectiongroup.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinGroupResponse;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detectiongroup.repository.DetectionGroupRepository;
import com.coinsensor.exchange.entity.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DetectionGroupServiceImpl implements DetectionGroupService {
    
    private final DetectionGroupRepository detectionGroupRepository;
    private final DetectedCoinRepository detectedCoinRepository;
    
    @Override
    public List<DetectedCoinGroupResponse> getDetectionGroups(String exchange, String exchangeType, String timeframe) {
        LocalDateTime startTime = getStartTimeByTimeframe(timeframe);
        Exchange.Type type = Exchange.Type.valueOf(exchangeType);
        
        return detectionGroupRepository.findByExchangeAndTimeframeAndAfterTime(exchange, type, timeframe, startTime)
                .stream()
                .map(group -> {
                    var detectedCoins = detectedCoinRepository.findByDetectionGroup(group);
                    
                    return DetectedCoinGroupResponse.builder()
                            .exchangeName(group.getExchange().getName())
                            .exchangeType(group.getExchange().getType().name())
                            .timeframeLabel(timeframe)
                            .criteriaVolatility(group.getDetectionCriteria().getVolatility())
                            .criteriaVolume(group.getDetectionCriteria().getVolume())
                            .detectedAt(group.getDetectedAt())
                            .coins(detectedCoins.stream()
                                    .map(DetectedCoinResponse::from)
                                    .toList())
                            .build();
                })
                .toList();
    }
    
    private LocalDateTime getStartTimeByTimeframe(String timeframe) {
        LocalDateTime now = LocalDateTime.now();
        return switch (timeframe) {
            case "1m" -> now.minusMinutes(30);
            case "5m" -> now.minusHours(1);
            case "15m" -> now.minusHours(4);
            case "1h" -> now.minusHours(24);
            case "4h" -> now.minusDays(1);
            case "1d" -> now.minusDays(7);
            default -> now.minusHours(1);
        };
    }
}