package com.coinsensor.ohlcvs.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.coinsensor.ohlcvs.entity.Ohlcv;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OhlcvResponse {
	private Long ohlcvId;
	private Long exchangeCoinId;
	private BigDecimal open;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private BigDecimal volume;
	private BigDecimal quoteVolume;
	private Long tradesCount;
	private LocalDateTime createdAt;

	public static OhlcvResponse from(Ohlcv entity) {
		return OhlcvResponse.builder()
			.ohlcvId(entity.getOhlcvId())
			.exchangeCoinId(entity.getExchangeCoin().getExchangeCoinId())
			.open(entity.getOpen())
			.high(entity.getHigh())
			.low(entity.getLow())
			.close(entity.getClose())
			.volume(entity.getVolume())
			.createdAt(entity.getCreatedAt())
			.build();
	}
}