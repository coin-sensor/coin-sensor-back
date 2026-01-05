package com.coinsensor.detection.service;

import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.detection.dto.response.DetectionChartResponse;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.dto.response.TopDetectedCoinResponse;

public interface DetectionService {
	List<DetectionInfoResponse> getDetections(String exchange, String exchangeType, String coinCategory,
		String timeframe);

	DetectionChartResponse getDetectionChart(String timeframe, LocalDateTime startTime, LocalDateTime endTime);

	List<TopDetectedCoinResponse> getTopDetectedCoins(String timeframe, LocalDateTime startTime, LocalDateTime endTime);
}