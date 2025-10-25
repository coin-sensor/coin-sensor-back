package com.coinsensor.detectedcoin.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
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
                .sorted((a, b) -> Double.compare(b.getVolatility(), a.getVolatility()))
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
}
