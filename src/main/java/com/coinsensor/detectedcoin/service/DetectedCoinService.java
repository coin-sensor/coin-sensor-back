package com.coinsensor.detectedcoin.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinGroupResponse;
import com.coinsensor.exchangecoin.entity.ExchangeCoin.ExchangeType;
import java.util.List;

public interface DetectedCoinService {
    List<DetectedCoinResponse> getAbnormalCoins();
    List<DetectedCoinResponse> getVolatileCoins();
    List<DetectedCoinResponse> getDetectedCoinsByTimeAndType(ExchangeType exchangeType);
    DetectedCoinGroupResponse getDetectedCoinGroupByTimeAndType(ExchangeType exchangeType);
}
