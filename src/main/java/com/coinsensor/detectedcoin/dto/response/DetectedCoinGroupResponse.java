package com.coinsensor.detectedcoin.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectedCoinGroupResponse {
    private String timeframeLabel;
    private BigDecimal criteriaVolatility;
    private Double criteriaVolume;
    private LocalDateTime detectedAt;
    private List<DetectedCoinResponse> coins;
}