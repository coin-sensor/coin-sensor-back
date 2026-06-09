package com.coinsensor.detection.service;

import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.detection.dto.response.DetectionChartResponse;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.exchange.entity.Exchange;

public interface DetectionService {
	List<DetectionInfoResponse> getDetectionInfos(String exchange, String exchangeType, String coinCategory,
		String timeframe);

	DetectionChartResponse getDetectionChart(String timeframe, LocalDateTime startTime, LocalDateTime endTime);

	void detectByTimeframe(String timeframeName, Exchange.Type exchangeType);
}