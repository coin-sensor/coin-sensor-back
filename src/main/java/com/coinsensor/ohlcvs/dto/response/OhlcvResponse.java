package com.coinsensor.ohlcvs.dto.response;

import com.coinsensor.ohlcvs.entity.Ohlcv;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OhlcvResponse {
    private Long ohlcvId;
    private Long exchangeCoinId;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private Long volume;
    private BigDecimal quoteVolume;
    private Long tradesCount;
    private LocalDateTime createdAt;
    
    public static OhlcvResponse from(Ohlcv entity) {
        return OhlcvResponse.builder()
                .ohlcvId(entity.getOhlcvId())
                .exchangeCoinId(entity.getExchangeCoin().getExchangeCoinId())
                .open(entity.getOpen())
                .high(entity.getHigh())
                .low(entity.getLow())
                .close(entity.getClose())
                .volume(entity.getVolume())
                .quoteVolume(entity.getQuoteVolume())
                .tradesCount(entity.getTradesCount())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}