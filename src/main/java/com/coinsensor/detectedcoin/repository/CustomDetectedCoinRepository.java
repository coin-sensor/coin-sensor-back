package com.coinsensor.detectedcoin.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.detection.dto.response.TopDetectedCoinResponse;

public interface CustomDetectedCoinRepository {
    
    List<TopDetectedCoinResponse> findTopDetectedCoins(String timeframe, LocalDateTime startTime, LocalDateTime endTime, int limit);
}