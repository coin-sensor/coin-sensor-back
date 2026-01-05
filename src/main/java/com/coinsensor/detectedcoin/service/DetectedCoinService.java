package com.coinsensor.detectedcoin.service;

import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.detection.dto.response.TopDetectedCoinResponse;

public interface DetectedCoinService {

	Long viewDetectedCoin(String uuid, Long detectedCoinId);

	List<TopDetectedCoinResponse> getTopDetectedCoins(String timeframe, LocalDateTime startTime, LocalDateTime endTime);
}
