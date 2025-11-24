package com.coinsensor.detectedcoin.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.userreaction.dto.response.ReactionCountResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectedCoinResponse {
	private Long detectedCoinId;
	private Long exchangeCoinId;
	private String coinTicker;
	private BigDecimal changeX;
	private BigDecimal volumeX;
	private Long viewCount;
	private LocalDateTime detectedAt;
	private Long likeCount;
	private Long dislikeCount;

	public static DetectedCoinResponse from(DetectedCoin detectedCoin) {
		return DetectedCoinResponse.builder()
			.detectedCoinId(detectedCoin.getDetectedCoinId())
			.exchangeCoinId(detectedCoin.getExchangeCoin().getExchangeCoinId())
			.coinTicker(detectedCoin.getCoin().getCoinTicker())
			.changeX(detectedCoin.getChangeX())
			.volumeX(detectedCoin.getVolumeX())
			.viewCount(detectedCoin.getViewCount())
			.detectedAt(detectedCoin.getDetectedAt())
			.build();
	}

	public static DetectedCoinResponse of(DetectedCoin detectedCoin, List<ReactionCountResponse> reactionCounts) {
		Long likeCount = reactionCounts.stream()
			.filter(r -> "like".equals(r.reactionName()))
			.findFirst()
			.map(ReactionCountResponse::count)
			.orElse(0L);
		
		Long dislikeCount = reactionCounts.stream()
			.filter(r -> "dislike".equals(r.reactionName()))
			.findFirst()
			.map(ReactionCountResponse::count)
			.orElse(0L);
		
		return DetectedCoinResponse.builder()
			.detectedCoinId(detectedCoin.getDetectedCoinId())
			.exchangeCoinId(detectedCoin.getExchangeCoin().getExchangeCoinId())
			.coinTicker(detectedCoin.getCoin().getCoinTicker())
			.changeX(detectedCoin.getChangeX())
			.volumeX(detectedCoin.getVolumeX())
			.viewCount(detectedCoin.getViewCount())
			.detectedAt(detectedCoin.getDetectedAt())
			.likeCount(likeCount)
			.dislikeCount(dislikeCount)
			.build();
	}
}
