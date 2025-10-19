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
    
    public static CoinResponse from(Coin coin) {
        return CoinResponse.builder()
                .coinId(coin.getCoinId())
                .coinTicker(coin.getCoinTicker())
                .baseAsset(coin.getBaseAsset())
                .build();
    }
}
