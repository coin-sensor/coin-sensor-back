package com.coinsensor.favoritecoin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCoinResponse {
	private Long exchangeCoinId;
	private String coinTicker;
	private String exchangeName;
	private String exchangeType;
}