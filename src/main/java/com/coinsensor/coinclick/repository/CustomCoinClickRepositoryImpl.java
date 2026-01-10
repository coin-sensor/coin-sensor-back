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
	public List<CoinViewCountResponse> findTopViewedCoins(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
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
	public List<CoinTrendDataResponse> findCoinsTrendData(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);

		// 1. 상위 코인들 조회
		List<Long> topCoinIds = queryFactory
			.select(coin.coinId)
			.from(coinClick)
			.join(coinClick.detectedCoin, detectedCoin)
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(coinClick.clickedAt.goe(startTime))
			.groupBy(coin.coinId)
			.orderBy(coinClick.count.sum().desc())
			.limit(limit)
			.fetch();

		if (topCoinIds.isEmpty()) {
			return new ArrayList<>();
		}

		// 2. 모든 데이터를 한 번에 조회
		List<Tuple> allData = queryFactory
			.select(
				coin.coinId,
				coin.coinTicker,
				coin.baseAsset,
				Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", coinClick.clickedAt),
				coinClick.count.sum())
			.from(coinClick)
			.join(coinClick.detectedCoin, detectedCoin)
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(exchangeCoin.coin.coinId.in(topCoinIds)
				.and(coinClick.clickedAt.goe(startTime)))
			.groupBy(
				coin.coinId,
				coin.coinTicker,
				coin.baseAsset,
				Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", coinClick.clickedAt))
			.orderBy(coin.coinId.asc(),
				Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", coinClick.clickedAt).asc())
			.fetch();

		// 3. 데이터 그룹핑 및 변환
		Map<Long, List<Tuple>> groupedData = allData.stream()
			.collect(Collectors.groupingBy(tuple -> tuple.get(coin.coinId)));

		return topCoinIds.stream()
			.map(coinId -> {
				List<Tuple> coinData = groupedData.getOrDefault(coinId, new ArrayList<>());
				if (coinData.isEmpty()) {
					return null;
				}

				Tuple firstTuple = coinData.get(0);
				String coinTicker = firstTuple.get(coin.coinTicker);
				String baseAsset = firstTuple.get(coin.baseAsset);

				Map<String, Long> dataMap = coinData.stream()
					.collect(Collectors.toMap(
						tuple -> tuple.get(3, String.class),
						tuple -> tuple.get(4, Long.class)));

				LocalDateTime now = LocalDateTime.now();
				List<CoinTrendDataResponse.TrendDataPoint> trendDataList = new ArrayList<>();

				long cumulativeSum = 0;
				LocalDateTime current = startTime.withMinute(0).withSecond(0).withNano(0);

				while (!current.isAfter(now)) {
					String timeKey = current.toString().replace("T", " ") + ":00";
					Long count = dataMap.getOrDefault(timeKey, 0L);
					cumulativeSum += count;

					trendDataList.add(new CoinTrendDataResponse.TrendDataPoint(current, cumulativeSum));

					if (current.withMinute(0).withSecond(0).withNano(0)
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
			.filter(response -> response != null)
			.toList();
	}

}