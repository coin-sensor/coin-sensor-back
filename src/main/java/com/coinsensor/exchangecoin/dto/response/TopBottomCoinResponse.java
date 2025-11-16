package com.coinsensor.exchangecoin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopBottomCoinResponse {
	private String symbol;
	private Double priceChangePercent;
}
