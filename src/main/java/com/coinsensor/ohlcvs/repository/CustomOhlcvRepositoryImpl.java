package com.coinsensor.ohlcvs.repository;

import static com.coinsensor.ohlcvs.entity.QOhlcv.*;

import java.time.LocalDateTime;
import java.util.List;

import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.ohlcvs.entity.Ohlcv;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomOhlcvRepositoryImpl implements CustomOhlcvRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<Ohlcv> findRecentCandles(ExchangeCoin exchangeCoin, String timeframeName, LocalDateTime startTime1, LocalDateTime startTime2) {
		return queryFactory
			.selectFrom(ohlcv)
			.where(
				ohlcv.exchangeCoin.exchangeCoinId.eq(exchangeCoin.getExchangeCoinId())
					.and(ohlcv.timeframe.name.eq(timeframeName))
					.and(ohlcv.startTime.in(startTime1, startTime2))
			)
			.orderBy(ohlcv.startTime.desc())
			.limit(2)
			.fetch();
	}

	@Override
	public long deleteByCreatedAtBefore(LocalDateTime cutoffDate) {
		return queryFactory
			.delete(ohlcv)
			.where(ohlcv.createdAt.lt(cutoffDate))
			.execute();
	}
}