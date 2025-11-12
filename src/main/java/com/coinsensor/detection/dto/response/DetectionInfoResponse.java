package com.coinsensor.detection.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detection.entity.Detection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetectionInfoResponse {
	private String exchangeName;
	private String exchangeType;
	private String timeframeLabel;
	private BigDecimal criteriaVolatility;
	private Double criteriaVolume;
	private LocalDateTime detectedAt;
	private List<DetectedCoinResponse> coins;

	public static DetectionInfoResponse of(Detection detection, List<DetectedCoin> detectedCoins) {
		return DetectionInfoResponse.builder()
			.exchangeName(detection.getExchange().getName())
			.exchangeType(detection.getExchange().getType().name())
			.timeframeLabel(detection.getDetectionCriteria().getTimeframe().getTimeframeLabel())
			.criteriaVolatility(detection.getDetectionCriteria().getVolatility())
			.criteriaVolume(detection.getDetectionCriteria().getVolume())
			.detectedAt(detection.getDetectedAt())
			.coins(detectedCoins.stream()
				.map(DetectedCoinResponse::from)
				.toList())
			.build();
	}
}