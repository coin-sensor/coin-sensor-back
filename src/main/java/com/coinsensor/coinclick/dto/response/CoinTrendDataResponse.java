package com.coinsensor.coinclick.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinTrendDataResponse {
	private Long coinId;
	private String baseAsset;
	private String coinTicker;
	private List<TrendDataPoint> data;

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TrendDataPoint {
		private LocalDateTime timestamp;
		private Long viewCount;
	}
}