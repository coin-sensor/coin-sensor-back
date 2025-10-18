package com.coinsensor.detectedcoin.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import java.util.List;

public interface DetectedCoinService {
    List<DetectedCoinResponse> getAbnormalCoins();
    List<DetectedCoinResponse> getVolatileCoins();
}
