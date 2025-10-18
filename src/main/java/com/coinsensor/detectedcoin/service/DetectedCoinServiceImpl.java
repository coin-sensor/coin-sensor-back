package com.coinsensor.detectedcoin.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
}
