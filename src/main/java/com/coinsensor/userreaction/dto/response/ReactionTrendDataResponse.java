package com.coinsensor.userreaction.dto.response;

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
public class ReactionTrendDataResponse {
	private Long coinId;
	private String coinTicker;
	private String baseAsset;
	private List<TrendDataPoint> data;

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TrendDataPoint {
		private LocalDateTime timestamp;
		private Long reactionCount;
	}
}