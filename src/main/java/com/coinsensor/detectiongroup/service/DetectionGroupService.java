package com.coinsensor.detectiongroup.service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinGroupResponse;
import java.util.List;

public interface DetectionGroupService {
    List<DetectedCoinGroupResponse> getDetectionGroups(String exchange, String exchangeType, String timeframe);
}