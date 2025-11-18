package com.coinsensor.detectedcoin.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.coinsensor.detectedcoin.entity.DetectedCoin;

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
	private String coinTicker;
	private BigDecimal changeX;
	private BigDecimal volumeX;
	private Long viewCount;
	private LocalDateTime detectedAt;

	public static DetectedCoinResponse from(DetectedCoin detectedCoin) {
		return DetectedCoinResponse.builder()
			.detectedCoinId(detectedCoin.getDetectedCoinId())
			.coinTicker(detectedCoin.getCoin().getCoinTicker())
			.changeX(detectedCoin.getChangeX())
			.volumeX(detectedCoin.getVolumeX())
			.viewCount(detectedCoin.getViewCount())
			.detectedAt(detectedCoin.getDetectedAt())
			.build();
	}
}
