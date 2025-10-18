package com.coinsensor.coin.dto.response;

import com.coinsensor.coin.entity.Coin;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoinResponse {
    private Long coinId;
    private String coinTicker;
    private String baseAsset;
    private String quoteAsset;
    private String name;
    private Boolean isActive;
    
    public static CoinResponse from(Coin coin) {
        return CoinResponse.builder()
                .coinId(coin.getCoinId())
                .coinTicker(coin.getCoinTicker())
                .baseAsset(coin.getBaseAsset())
                .quoteAsset(coin.getQuoteAsset())
                .name(coin.getName())
                .isActive(coin.getIsActive())
                .build();
    }
}
