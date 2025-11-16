package com.coinsensor.detection.service;

import java.util.List;

import com.coinsensor.detection.dto.response.DetectionInfoResponse;

public interface DetectionService {
	List<DetectionInfoResponse> getDetections(String exchange, String exchangeType, String coinRanking,
		String timeframe);
}