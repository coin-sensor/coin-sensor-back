package com.coinsensor.detectedcoin.dto.response;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectedCoinResponse {
    private Long detectedCoinId;
    private String coinTicker;
    private BigDecimal volatility;
    private Double volume;
    private LocalDateTime detectedAt;
    
    public static DetectedCoinResponse from(DetectedCoin detectedCoin) {
        return DetectedCoinResponse.builder()
                .detectedCoinId(detectedCoin.getDetectedCoinId())
                .coinTicker(detectedCoin.getCoin().getCoinTicker())
                .volatility(detectedCoin.getVolatility())
                .volume(detectedCoin.getVolume())
                .detectedAt(detectedCoin.getDetectedAt())
                .build();
    }
}
