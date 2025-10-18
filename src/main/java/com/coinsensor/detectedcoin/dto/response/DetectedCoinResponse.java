package com.coinsensor.detectedcoin.dto.response;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectedCoinResponse {
    private Long detectedCoinId;
    private Long coinId;
    private Double volatility;
    private Double volume;
    private LocalDateTime createdAt;
    
    public static DetectedCoinResponse from(DetectedCoin detectedCoin) {
        return DetectedCoinResponse.builder()
                .detectedCoinId(detectedCoin.getDetectedCoinId())
                .coinId(detectedCoin.getCoin().getCoinId())
                .volatility(detectedCoin.getVolatility())
                .volume(detectedCoin.getVolume())
                .createdAt(detectedCoin.getCreatedAt())
                .build();
    }
}
