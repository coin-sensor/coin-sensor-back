package com.coinsensor.detectedcoin.repository;

import static com.coinsensor.detectedcoin.entity.QDetectedCoin.*;
import static com.coinsensor.detection.entity.QDetection.*;
import static com.coinsensor.detectioncriteria.entity.QDetectionCriteria.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.timeframe.entity.Timeframe;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomDetectedCoinRepositoryImpl implements CustomDetectedCoinRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<DetectedCoin> finAllByTimeframe(Timeframe timeframe) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startOfPrevMinute = now.minusMinutes(1).truncatedTo(ChronoUnit.MINUTES);
		LocalDateTime endOfPrevMinute = startOfPrevMinute.plusMinutes(1).minusNanos(1);

		return queryFactory
			.selectFrom(detectedCoin)
			.join(detectedCoin.detection, detection).fetchJoin()
			.join(detection.detectionCriteria, detectionCriteria).fetchJoin()
			.where(detectionCriteria.timeframe.eq(timeframe)
				.and(detectedCoin.detectedAt.between(startOfPrevMinute, endOfPrevMinute)))
			.fetch();
	}
}