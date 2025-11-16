package com.coinsensor.detection.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.repository.DetectionRepository;
import com.coinsensor.exchange.entity.Exchange;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetectionServiceImpl implements DetectionService {

	private final DetectionRepository detectionRepository;
	private final DetectedCoinRepository detectedCoinRepository;

	@Override
	public List<DetectionInfoResponse> getDetections(String exchange, String exchangeType, String coinRanking,
		String timeframe) {
		LocalDateTime startTime = getStartTimeByTimeframe(timeframe);
		Exchange.Type type = Exchange.Type.valueOf(exchangeType);

		return detectionRepository.findByExchangeAndTimeframeAndAfterTime(exchange, type, timeframe, startTime)
			.stream()
			.map(detection -> {
				var detectedCoins = detectedCoinRepository.findByDetection(detection);

				return DetectionInfoResponse.builder()
					.exchangeName(detection.getExchange().getName())
					.exchangeType(detection.getExchange().getType().name())
					.timeframeLabel(timeframe)
					.criteriaVolatility(detection.getDetectionCriteria().getVolatility())
					.criteriaVolume(detection.getDetectionCriteria().getVolume())
					.detectedAt(detection.getDetectedAt())
					.coins(detectedCoins.stream()
						.map(DetectedCoinResponse::from)
						.toList())
					.build();
			})
			.toList();
	}

	private LocalDateTime getStartTimeByTimeframe(String timeframe) {
		LocalDateTime now = LocalDateTime.now();
		return switch (timeframe) {
			case "1m" -> now.minusMinutes(30);
			case "5m" -> now.minusHours(1);
			case "15m" -> now.minusHours(4);
			case "1h" -> now.minusHours(24);
			case "4h" -> now.minusDays(1);
			case "1d" -> now.minusDays(7);
			default -> now.minusHours(1);
		};
	}
}