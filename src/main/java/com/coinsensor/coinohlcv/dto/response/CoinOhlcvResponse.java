package com.coinsensor.coinohlcv.dto.response;

import com.coinsensor.coinohlcv.entity.CoinOhlcv;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoinOhlcvResponse {
    private Long coinOhlcvId;
    private Long coinId;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;
    private LocalDateTime createdAt;
    
    public static CoinOhlcvResponse from(CoinOhlcv entity) {
        return CoinOhlcvResponse.builder()
                .coinOhlcvId(entity.getCoinOhlcvId())
                .coinId(entity.getCoin().getCoinId())
                .open(entity.getOpen())
                .high(entity.getHigh())
                .low(entity.getLow())
                .close(entity.getClose())
                .volume(entity.getVolume())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
