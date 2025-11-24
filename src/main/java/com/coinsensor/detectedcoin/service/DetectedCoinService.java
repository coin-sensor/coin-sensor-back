package com.coinsensor.detectedcoin.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;

import java.util.List;

public interface DetectedCoinService {
    List<DetectedCoinResponse> getAbnormalCoins();
    List<DetectedCoinResponse> getVolatileCoins();
    List<DetectedCoinResponse> getDetectedCoinsByTimeAndType(String exchangeName, String exchangeType);
    Long viewDetectedCoin(String uuid, Long detectedCoinId);
}
