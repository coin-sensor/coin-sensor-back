package com.coinsensor.userreaction.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReactionTrendDataResponse {
	private final Long coinId;
	private final String coinTicker;
	private final String baseAsset;
	private final List<TrendDataPoint> likeData;
	private final List<TrendDataPoint> dislikeData;

	@Getter
	@AllArgsConstructor
	public static class TrendDataPoint {
		private final LocalDateTime timestamp;
		private final Long reactionCount;
	}
}