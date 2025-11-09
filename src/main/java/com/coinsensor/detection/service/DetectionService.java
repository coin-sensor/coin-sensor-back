package com.coinsensor.detection.service;

import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import java.util.List;

public interface DetectionService {
    List<DetectionInfoResponse> getDetections(String exchange, String exchangeType, String timeframe);
}