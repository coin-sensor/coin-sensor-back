package com.coinsensor.favoritecoin.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteCoinResponse {
	private Long exchangeCoinId;
	private String coinTicker;
	private String exchangeName;
	private String exchangeType;
}