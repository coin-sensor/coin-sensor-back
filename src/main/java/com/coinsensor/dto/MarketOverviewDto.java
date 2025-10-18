package com.coinsensor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarketOverviewDto {
    private BigDecimal bitcoinPrice;
    private BigDecimal bitcoinChangePercent;
    private Integer fearGreedIndex;
    private String fearGreedLabel;
    private BigDecimal longShortRatio;
    private BigDecimal kimchiPremium;
    private Integer totalCoins;
    private Integer abnormalCoins;
    private LocalDateTime lastUpdated;
}