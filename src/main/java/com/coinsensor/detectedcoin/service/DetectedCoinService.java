package com.coinsensor.detectedcoin.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinGroupResponse;
import java.util.List;

public interface DetectedCoinService {
    List<DetectedCoinResponse> getAbnormalCoins();
    List<DetectedCoinResponse> getVolatileCoins();
    List<DetectedCoinResponse> getDetectedCoinsByTimeAndType(String exchangeName, String exchangeType);
    DetectedCoinGroupResponse getDetectedCoinGroupByTimeAndType(String exchangeName, String timeframeLabel, String exchangeType);
}
