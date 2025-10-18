package com.coinsensor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinDataDto {
    private String symbol;
    private String name;
    private BigDecimal currentPrice;
    private BigDecimal volume24h;
    private BigDecimal priceChangePercent24h;
    private BigDecimal high24h;
    private BigDecimal low24h;
    private LocalDateTime lastUpdated;
    private Boolean isAbnormal;
    private BigDecimal volatilityScore;
}