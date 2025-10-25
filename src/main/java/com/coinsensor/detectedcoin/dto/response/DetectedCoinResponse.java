package com.coinsensor.detectedcoin.dto.response;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.exchangecoin.entity.ExchangeCoin.ExchangeType;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectedCoinResponse {
    private Long detectedCoinId;
    private String coinTicker;
    private Double volatility;
    private Double volume;
    private LocalDateTime createdAt;
    private ExchangeType exchangeType;
    
    public static DetectedCoinResponse from(DetectedCoin detectedCoin) {
        return DetectedCoinResponse.builder()
                .detectedCoinId(detectedCoin.getDetectedCoinId())
                .coinTicker(detectedCoin.getCoin().getCoinTicker())
                .volatility(detectedCoin.getVolatility())
                .volume(detectedCoin.getVolume())
                .createdAt(detectedCoin.getCreatedAt())
                .exchangeType(detectedCoin.getExchangeCoin().getExchangeType())
                .build();
    }
}
