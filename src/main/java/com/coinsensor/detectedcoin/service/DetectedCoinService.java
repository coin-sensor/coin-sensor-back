package com.coinsensor.detectedcoin.service;

import java.util.List;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;

public interface DetectedCoinService {
	List<DetectedCoinResponse> getDetectedCoinsByTimeAndType(String exchangeName, String exchangeType);

	Long viewDetectedCoin(String uuid, Long detectedCoinId);
}
