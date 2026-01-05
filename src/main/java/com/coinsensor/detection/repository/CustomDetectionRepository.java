package com.coinsensor.detection.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.exchange.entity.Exchange;

public interface CustomDetectionRepository {

	List<DetectionInfoResponse> getDetectionInfos(String exchange, Exchange.Type exchangeType,
		String timeframe, LocalDateTime startTime, List<String> targetTickers);
}