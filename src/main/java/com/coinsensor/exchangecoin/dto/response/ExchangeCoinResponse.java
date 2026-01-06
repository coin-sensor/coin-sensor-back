package com.coinsensor.exchangecoin.dto.response;

import com.coinsensor.exchangecoin.entity.ExchangeCoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeCoinResponse {

	private Long exchangeCoinId;
	private String exchangeName;
	private String exchangeType;
	private String coinTicker;
	private String baseAsset;
	private Boolean isActive;
	private Boolean enableDetection;

	public static ExchangeCoinResponse from(ExchangeCoin exchangeCoin) {
		return ExchangeCoinResponse.builder()
			.exchangeCoinId(exchangeCoin.getExchangeCoinId())
			.exchangeName(exchangeCoin.getExchange().getName())
			.exchangeType(String.valueOf(exchangeCoin.getExchange().getType()))
			.coinTicker(exchangeCoin.getCoin().getCoinTicker())
			.baseAsset(exchangeCoin.getCoin().getBaseAsset())
			.isActive(exchangeCoin.getIsActive())
			.enableDetection(exchangeCoin.getEnableDetection())
			.build();
	}
}