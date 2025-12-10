package com.coinsensor.clickcoin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoinViewCountResponse {
	private Long coinId;
	private String coinTicker;
	private String baseAsset;
	private Long totalViewCount;
}
