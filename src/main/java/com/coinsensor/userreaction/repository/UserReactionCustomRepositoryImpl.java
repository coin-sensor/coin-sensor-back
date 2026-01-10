package com.coinsensor.userreaction.repository;

import static com.coinsensor.coin.entity.QCoin.*;
import static com.coinsensor.detectedcoin.entity.QDetectedCoin.*;
import static com.coinsensor.exchangecoin.entity.QExchangeCoin.*;
import static com.coinsensor.userreaction.entity.QUserReaction.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.coinsensor.user.entity.User;
import com.coinsensor.userreaction.dto.response.CoinReactionResponse;
import com.coinsensor.userreaction.dto.response.ReactionTrendDataResponse;
import com.coinsensor.userreaction.entity.UserReaction;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserReactionCustomRepositoryImpl implements UserReactionCustomRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<UserReaction> findByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId) {

		UserReaction result = queryFactory
			.selectFrom(userReaction)
			.where(
				userReaction.user.eq(user)
					.and(userReaction.targetType.eq(targetType))
					.and(userReaction.targetId.eq(targetId))
			)
			.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public List<CoinReactionResponse> findTopLikedCoins(LocalDateTime startTime, int limit) {
		return queryFactory
			.select(Projections.constructor(CoinReactionResponse.class,
				coin.coinTicker,
				coin.baseAsset,
				detectedCoin.likeCount.sum()))
			.from(detectedCoin)
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(detectedCoin.detectedAt.goe(startTime)
				.and(detectedCoin.likeCount.gt(0L)))
			.groupBy(coin.coinTicker, coin.baseAsset)
			.orderBy(detectedCoin.likeCount.sum().desc())
			.limit(limit)
			.fetch();
	}

	@Override
	public List<CoinReactionResponse> findTopDislikedCoins(LocalDateTime startTime, int limit) {
		return queryFactory
			.select(Projections.constructor(CoinReactionResponse.class,
				coin.coinTicker,
				coin.baseAsset,
				detectedCoin.dislikeCount.sum()))
			.from(detectedCoin)
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(detectedCoin.detectedAt.goe(startTime)
				.and(detectedCoin.dislikeCount.gt(0L)))
			.groupBy(coin.coinTicker, coin.baseAsset)
			.orderBy(detectedCoin.dislikeCount.sum().desc())
			.limit(limit)
			.fetch();
	}

	@Override
	public List<ReactionTrendDataResponse> findLikeTrendData(LocalDateTime startTime, int limit) {
		// 1. 상위 코인들 조회
		List<Long> topCoinIds = queryFactory
			.select(coin.coinId)
			.from(userReaction)
			.join(detectedCoin).on(userReaction.targetId.eq(detectedCoin.detectedCoinId))
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(userReaction.createdAt.goe(startTime)
				.and(userReaction.targetType.eq("detected_coins"))
				.and(userReaction.reaction.name.eq("like")))
			.groupBy(coin.coinId)
			.orderBy(userReaction.count().desc())
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
				Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", userReaction.createdAt),
				userReaction.count())
			.from(userReaction)
			.join(detectedCoin).on(userReaction.targetId.eq(detectedCoin.detectedCoinId))
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(exchangeCoin.coin.coinId.in(topCoinIds)
				.and(userReaction.createdAt.goe(startTime))
				.and(userReaction.targetType.eq("detected_coins"))
				.and(userReaction.reaction.name.eq("like")))
			.groupBy(
				coin.coinId,
				coin.coinTicker,
				coin.baseAsset,
				Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", userReaction.createdAt))
			.orderBy(coin.coinId.asc(),
				Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", userReaction.createdAt).asc())
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

				Map<String, Long> likeDataMap = coinData.stream()
					.collect(Collectors.toMap(
						tuple -> tuple.get(3, String.class),
						tuple -> tuple.get(4, Long.class)));

				LocalDateTime now = LocalDateTime.now();
				List<ReactionTrendDataResponse.TrendDataPoint> trendDataList = new ArrayList<>();

				long cumulativeSum = 0;
				LocalDateTime current = startTime.withMinute(0).withSecond(0).withNano(0);

				while (!current.isAfter(now)) {
					String timeKey = current.toString().replace("T", " ") + ":00";
					Long count = likeDataMap.getOrDefault(timeKey, 0L);
					cumulativeSum += count;

					trendDataList.add(new ReactionTrendDataResponse.TrendDataPoint(current, cumulativeSum));

					if (current.withMinute(0).withSecond(0).withNano(0)
						.equals(now.withMinute(0).withSecond(0).withNano(0)) && now.getMinute() > 0) {
						trendDataList.add(new ReactionTrendDataResponse.TrendDataPoint(now, cumulativeSum));
					}

					current = current.plusHours(1);
				}

				return ReactionTrendDataResponse.builder()
					.coinId(coinId)
					.baseAsset(baseAsset)
					.coinTicker(coinTicker)
					.data(trendDataList)
					.build();
			})
			.filter(response -> response != null)
			.toList();
	}

	@Override
	public List<ReactionTrendDataResponse> findDislikeTrendData(LocalDateTime startTime, int limit) {
		// 1. 상위 코인들 조회
		List<Long> topCoinIds = queryFactory
			.select(coin.coinId)
			.from(userReaction)
			.join(detectedCoin).on(userReaction.targetId.eq(detectedCoin.detectedCoinId))
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(userReaction.createdAt.goe(startTime)
				.and(userReaction.targetType.eq("detected_coins"))
				.and(userReaction.reaction.name.eq("dislike")))
			.groupBy(coin.coinId)
			.orderBy(userReaction.count().desc())
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
				Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", userReaction.createdAt),
				userReaction.count())
			.from(userReaction)
			.join(detectedCoin).on(userReaction.targetId.eq(detectedCoin.detectedCoinId))
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(exchangeCoin.coin.coinId.in(topCoinIds)
				.and(userReaction.createdAt.goe(startTime))
				.and(userReaction.targetType.eq("detected_coins"))
				.and(userReaction.reaction.name.eq("dislike")))
			.groupBy(
				coin.coinId,
				coin.coinTicker,
				coin.baseAsset,
				Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", userReaction.createdAt))
			.orderBy(coin.coinId.asc(),
				Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", userReaction.createdAt).asc())
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

				Map<String, Long> dislikeDataMap = coinData.stream()
					.collect(Collectors.toMap(
						tuple -> tuple.get(3, String.class),
						tuple -> tuple.get(4, Long.class)));

				LocalDateTime now = LocalDateTime.now();
				List<ReactionTrendDataResponse.TrendDataPoint> trendDataList = new ArrayList<>();

				long cumulativeSum = 0;
				LocalDateTime current = startTime.withMinute(0).withSecond(0).withNano(0);

				while (!current.isAfter(now)) {
					String timeKey = current.toString().replace("T", " ") + ":00";
					Long count = dislikeDataMap.getOrDefault(timeKey, 0L);
					cumulativeSum += count;

					trendDataList.add(new ReactionTrendDataResponse.TrendDataPoint(current, cumulativeSum));

					if (current.withMinute(0).withSecond(0).withNano(0)
						.equals(now.withMinute(0).withSecond(0).withNano(0)) && now.getMinute() > 0) {
						trendDataList.add(new ReactionTrendDataResponse.TrendDataPoint(now, cumulativeSum));
					}

					current = current.plusHours(1);
				}

				return ReactionTrendDataResponse.builder()
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