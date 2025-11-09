package com.coinsensor.detection.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectionInfoResponse {
    private String exchangeName;
    private String exchangeType;
    private String timeframeLabel;
    private BigDecimal criteriaVolatility;
    private Double criteriaVolume;
    private LocalDateTime detectedAt;
    private List<DetectedCoinResponse> coins;
}