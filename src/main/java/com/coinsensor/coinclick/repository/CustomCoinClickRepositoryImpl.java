package com.coinsensor.coinclick.repository;

import static com.coinsensor.coin.entity.QCoin.*;
import static com.coinsensor.coinclick.entity.QCoinClick.*;
import static com.coinsensor.detectedcoin.entity.QDetectedCoin.*;
import static com.coinsensor.exchangecoin.entity.QExchangeCoin.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.coinsensor.coinclick.dto.response.CoinTrendDataResponse;
import com.coinsensor.coinclick.dto.response.CoinViewCountResponse;
import com.coinsensor.coinclick.entity.CoinClick;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomCoinClickRepositoryImpl implements CustomCoinClickRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<CoinClick> findByUuidAndDetectedCoinId(String uuid, Long detectedCoinId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(coinClick)
				.where(coinClick.user.uuid.eq(uuid)
					.and(coinClick.detectedCoin.detectedCoinId.eq(detectedCoinId)))
				.fetchOne());
	}

	@Override
	public List<CoinViewCountResponse> findTopViewedCoins(LocalDateTime startTime, int limit) {
		return queryFactory
			.select(Projections.constructor(CoinViewCountResponse.class,
				coin.coinId,
				coin.coinTicker,
				coin.baseAsset,
				coinClick.count.sum()))
			.from(coinClick)
			.join(coinClick.detectedCoin, detectedCoin)
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(coinClick.clickedAt.goe(startTime))
			.groupBy(coin.coinId, coin.coinTicker, coin.baseAsset)
			.orderBy(coinClick.count.sum().desc())
			.limit(limit)
			.fetch();
	}

	@Override
	public List<CoinTrendDataResponse> findCoinsTrendData(LocalDateTime startTime, int limit) {
		List<Tuple> topCoins = queryFactory
			.select(coin.coinId, coin.coinTicker, coin.baseAsset)
			.from(coinClick)
			.join(coinClick.detectedCoin, detectedCoin)
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(coinClick.clickedAt.goe(startTime))
			.groupBy(coin.coinId, coin.coinTicker, coin.baseAsset)
			.orderBy(coinClick.count.sum().desc())
			.limit(limit)
			.fetch();

		return topCoins.stream()
			.map(coinTuple -> {
				Long coinId = coinTuple.get(coin.coinId);
				String coinTicker = coinTuple.get(coin.coinTicker);
				String baseAsset = coinTuple.get(coin.baseAsset);

				List<Tuple> rawTrendData = queryFactory
					.select(Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", coinClick.clickedAt),
						coinClick.count.sum())
					.from(coinClick)
					.join(coinClick.detectedCoin, detectedCoin)
					.join(detectedCoin.exchangeCoin, exchangeCoin)
					.where(exchangeCoin.coin.coinId.eq(coinId)
						.and(coinClick.clickedAt.goe(startTime)))
					.groupBy(Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", coinClick.clickedAt))
					.orderBy(
						Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", coinClick.clickedAt).asc())
					.fetch();

				// 시간별 데이터를 맵으로 변환
				Map<String, Long> dataMap = rawTrendData.stream()
					.collect(Collectors.toMap(
						tuple -> tuple.get(0, String.class),
						tuple -> tuple.get(1, Long.class)));

				// 시작 시간부터 현재 시간까지 모든 시간대 생성
				LocalDateTime now = LocalDateTime.now();
				List<CoinTrendDataResponse.TrendDataPoint> trendDataList = new ArrayList<>();

				// 시간별 데이터 생성 및 누적 계산
				long cumulativeSum = 0;
				LocalDateTime current = startTime.withMinute(0).withSecond(0).withNano(0);

				while (!current.isAfter(now)) {
					String timeKey = current.toString().replace("T", " ") + ":00";
					Long count = dataMap.getOrDefault(timeKey, 0L);
					cumulativeSum += count;

					trendDataList.add(new CoinTrendDataResponse.TrendDataPoint(current, cumulativeSum));

					// 마지막 시간이면 현재 시간도 추가
					if (current.withMinute(0)
						.withSecond(0)
						.withNano(0)
						.equals(now.withMinute(0).withSecond(0).withNano(0)) && now.getMinute() > 0) {
						trendDataList.add(new CoinTrendDataResponse.TrendDataPoint(now, cumulativeSum));
					}

					current = current.plusHours(1);
				}

				return CoinTrendDataResponse.builder()
					.coinId(coinId)
					.baseAsset(baseAsset)
					.coinTicker(coinTicker)
					.data(trendDataList)
					.build();
			})
			.toList();
	}

}