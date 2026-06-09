package com.coinsensor.detection.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.conditions.entity.Condition;
import com.coinsensor.conditions.repository.ConditionRepository;
import com.coinsensor.detection.dto.response.DetectionChartResponse;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.entity.Detection;
import com.coinsensor.detection.repository.DetectionRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;
import com.coinsensor.exchangecoin.service.ExchangeCoinService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetectionServiceImpl implements DetectionService {
	private final DetectionRepository detectionRepository;
	private final ConditionRepository conditionRepository;
	private final ExchangeCoinService exchangeCoinService;
	private final DetectionProcessComponent klineDetectionService;

	@Override
	public List<DetectionInfoResponse> getDetectionInfos(String exchange, String exchangeType, String coinCategory,
		String timeframe) {
		Exchange.Type type = Exchange.Type.valueOf(exchangeType);

		List<String> targetTickers = getTargetTickersByCoinCategory(coinCategory, exchangeType);

		return detectionRepository.getDetectionInfos(exchange, type, timeframe, targetTickers);
	}

	@Deprecated
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

	private List<String> getTargetTickersByCoinCategory(String coinCategory, String exchangeType) {
		return switch (coinCategory) {
			case "top20" -> exchangeCoinService.getTopCoins(exchangeType).stream()
				.map(TopBottomCoinResponse::getSymbol)
				.toList();
			case "bottom20" -> exchangeCoinService.getBottomCoins(exchangeType).stream()
				.map(TopBottomCoinResponse::getSymbol)
				.toList();
			default -> List.of(); // 빈 리스트는 모든 코인을 의미
		};
	}

	@Override
	public DetectionChartResponse getDetectionChart(String timeframe, LocalDateTime startTime, LocalDateTime endTime) {
		List<Detection> detections = detectionRepository.findByTimeframeBetween(timeframe, startTime, endTime);

		Map<String, Long> detectionCounts = detections.stream()
			.collect(Collectors.groupingBy(
				detection -> formatDateTime(detection.getDetectedAt(), timeframe),
				Collectors.summingLong(Detection::getDetectionCount)
			));

		List<String> labels = new ArrayList<>(detectionCounts.keySet());
		labels.sort(String::compareTo);

		List<Integer> data = labels.stream()
			.map(label -> detectionCounts.getOrDefault(label, 0L).intValue())
			.toList();

		DetectionChartResponse.Dataset dataset = new DetectionChartResponse.Dataset("탐지 수", data);
		return new DetectionChartResponse(labels, List.of(dataset));
	}

	private String formatDateTime(LocalDateTime dateTime, String timeframe) {
		DateTimeFormatter formatter = switch (timeframe) {
			case "1m", "5m", "15m" -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			case "1h", "4h" -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");
			case "1d" -> DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00");
			default -> DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		};
		return dateTime.format(formatter);
	}

	public void detectByTimeframe(String timeframeName, Exchange.Type exchangeType) {
		// 해당 조건들 조회
		List<Condition> conditions = conditionRepository.findByTimeframeName(timeframeName)
			.orElseThrow(() -> new CustomException(ErrorCode.CONDITION_NOT_FOUND));

		// 모든 조건에 대해 비동기 탐지 실행
		for (Condition condition : conditions) {
			klineDetectionService.processConditionDetection(condition, exchangeType);
		}
	}

}