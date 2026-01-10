package com.coinsensor.detection.repository;

import static com.coinsensor.coin.entity.QCoin.*;
import static com.coinsensor.conditions.entity.QCondition.*;
import static com.coinsensor.detectedcoin.entity.QDetectedCoin.*;
import static com.coinsensor.detection.entity.QDetection.*;
import static com.coinsensor.exchange.entity.QExchange.*;
import static com.coinsensor.exchangecoin.entity.QExchangeCoin.*;
import static com.coinsensor.timeframe.entity.QTimeframe.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.entity.Detection;
import com.coinsensor.exchange.entity.Exchange;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomDetectionRepositoryImpl implements CustomDetectionRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<DetectionInfoResponse> getDetectionInfos(String exchangeName, Exchange.Type exchangeType,
		String timeframeName, LocalDateTime startTime, List<String> targetTickers) {

		// Detection 조회
		List<Detection> detections = queryFactory
			.selectFrom(detection)
			.join(detection.exchange, exchange).fetchJoin()
			.join(detection.condition, condition).fetchJoin()
			.join(condition.timeframe, timeframe).fetchJoin()
			.where(
				exchange.name.eq(exchangeName)
					.and(exchange.type.eq(exchangeType))
					.and(timeframe.name.eq(timeframeName))
					.and(detection.detectedAt.goe(startTime))
			)
			.orderBy(detection.detectedAt.desc())
			.fetch();

		if (detections.isEmpty()) {
			return List.of();
		}

		// DetectedCoin 조회
		List<DetectedCoin> detectedCoins = queryFactory
			.selectFrom(detectedCoin)
			.join(detectedCoin.exchangeCoin, exchangeCoin).fetchJoin()
			.join(exchangeCoin.coin, coin).fetchJoin()
			.where(
				detectedCoin.detection.in(detections)
					.and(targetTickers.isEmpty() ? null : coin.coinTicker.in(targetTickers))
			)
			.fetch();

		// Detection별로 DetectedCoin 그룹핑
		Map<Long, List<DetectedCoin>> coinsByDetection = detectedCoins.stream()
			.collect(Collectors.groupingBy(dc -> dc.getDetection().getDetectionId()));

		return detections.stream()
			.map(det -> {
				List<DetectedCoin> coins = coinsByDetection.getOrDefault(det.getDetectionId(), List.of());
				if (coins.isEmpty()) {
					return null;
				}
				List<DetectedCoinResponse> coinResponses = coins.stream()
					.map(DetectedCoinResponse::from)
					.toList();
				return DetectionInfoResponse.of(det, coinResponses);
			})
			.filter(Objects::nonNull)
			.toList();
	}

	@Override
	public List<Detection> findByTimeframeBetween(String timeframeName, LocalDateTime startTime,
		LocalDateTime endTime) {
		return queryFactory
			.selectFrom(detection)
			.join(detection.condition, condition).fetchJoin()
			.join(condition.timeframe, timeframe).fetchJoin()
			.where(
				timeframe.name.eq(timeframeName)
					.and(detection.detectedAt.goe(startTime))
					.and(detection.detectedAt.loe(endTime))
			)
			.orderBy(detection.detectedAt.asc())
			.fetch();
	}
}