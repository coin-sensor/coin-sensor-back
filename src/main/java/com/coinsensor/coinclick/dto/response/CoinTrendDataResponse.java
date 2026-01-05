package com.coinsensor.coinclick.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CoinTrendDataResponse {
	private Long coinId;
	private String baseAsset;
	private String coinTicker;
	private List<TrendDataPoint> data;

	@Getter
	@Builder
	public static class TrendDataPoint {
		private LocalDateTime timestamp;
		private Long viewCount;

		public TrendDataPoint(LocalDateTime timestamp, Long viewCount) {
			this.timestamp = timestamp;
			this.viewCount = viewCount;
		}
	}
}