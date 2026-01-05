package com.coinsensor.detectedcoin.repository;

import static com.coinsensor.coin.entity.QCoin.*;
import static com.coinsensor.conditions.entity.QCondition.*;
import static com.coinsensor.detectedcoin.entity.QDetectedCoin.*;
import static com.coinsensor.detection.entity.QDetection.*;
import static com.coinsensor.exchangecoin.entity.QExchangeCoin.*;
import static com.coinsensor.timeframe.entity.QTimeframe.*;

import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.detection.dto.response.TopDetectedCoinResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomDetectedCoinRepositoryImpl implements CustomDetectedCoinRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public List<TopDetectedCoinResponse> findTopDetectedCoins(String timeframeName, LocalDateTime startTime,
		LocalDateTime endTime, int limit) {
		return queryFactory
			.select(Projections.constructor(TopDetectedCoinResponse.class,
				coin.coinTicker,
				detectedCoin.count(),
				detectedCoin.changeX.max(),
				detectedCoin.volumeX.max()))
			.from(detectedCoin)
			.join(detectedCoin.detection, detection)
			.join(detection.condition, condition)
			.join(condition.timeframe, timeframe)
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(
				timeframe.name.eq(timeframeName)
					.and(detection.detectedAt.between(startTime, endTime))
			)
			.groupBy(coin.coinTicker)
			.orderBy(detectedCoin.count().desc())
			.limit(limit)
			.fetch();
	}
}