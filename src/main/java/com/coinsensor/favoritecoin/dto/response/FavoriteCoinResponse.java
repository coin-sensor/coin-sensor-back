package com.coinsensor.favoritecoin.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class FavoriteCoinResponse {
    private Long favoriteCoinId;
    private Long exchangeCoinId;
    private String coinTicker;
    private String baseAsset;
    private String exchangeName;
    private LocalDateTime createdAt;
}