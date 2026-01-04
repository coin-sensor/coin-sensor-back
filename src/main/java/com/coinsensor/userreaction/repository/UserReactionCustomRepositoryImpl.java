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
	public List<ReactionTrendDataResponse> findReactionsTrendData(LocalDateTime startTime, int limit) {
		List<Tuple> topCoins = queryFactory
			.select(coin.coinId, coin.coinTicker, coin.baseAsset)
			.from(detectedCoin)
			.join(detectedCoin.exchangeCoin, exchangeCoin)
			.join(exchangeCoin.coin, coin)
			.where(detectedCoin.detectedAt.goe(startTime)
				.and(detectedCoin.likeCount.add(detectedCoin.dislikeCount).gt(0L)))
			.groupBy(coin.coinId, coin.coinTicker, coin.baseAsset)
			.orderBy(detectedCoin.likeCount.add(detectedCoin.dislikeCount).sum().desc())
			.limit(limit)
			.fetch();

		return topCoins.stream()
			.map(coinTuple -> {
				Long coinId = coinTuple.get(coin.coinId);
				String coinTicker = coinTuple.get(coin.coinTicker);
				String baseAsset = coinTuple.get(coin.baseAsset);

				// Like 데이터 조회
				List<Tuple> likeData = queryFactory
					.select(
						Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", detectedCoin.detectedAt),
						detectedCoin.likeCount.sum())
					.from(detectedCoin)
					.join(detectedCoin.exchangeCoin, exchangeCoin)
					.where(exchangeCoin.coin.coinId.eq(coinId)
						.and(detectedCoin.detectedAt.goe(startTime))
						.and(detectedCoin.likeCount.gt(0L)))
					.groupBy(
						Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", detectedCoin.detectedAt))
					.orderBy(
						Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", detectedCoin.detectedAt)
							.asc())
					.fetch();

				// Dislike 데이터 조회
				List<Tuple> dislikeData = queryFactory
					.select(
						Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", detectedCoin.detectedAt),
						detectedCoin.dislikeCount.sum())
					.from(detectedCoin)
					.join(detectedCoin.exchangeCoin, exchangeCoin)
					.where(exchangeCoin.coin.coinId.eq(coinId)
						.and(detectedCoin.detectedAt.goe(startTime))
						.and(detectedCoin.dislikeCount.gt(0L)))
					.groupBy(
						Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", detectedCoin.detectedAt))
					.orderBy(
						Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d %H:00:00')", detectedCoin.detectedAt)
							.asc())
					.fetch();

				// Like 데이터 처리
				Map<String, Long> likeDataMap = likeData.stream()
					.collect(Collectors.toMap(
						tuple -> tuple.get(0, String.class),
						tuple -> tuple.get(1, Long.class)));

				// Dislike 데이터 처리
				Map<String, Long> dislikeDataMap = dislikeData.stream()
					.collect(Collectors.toMap(
						tuple -> tuple.get(0, String.class),
						tuple -> tuple.get(1, Long.class)));

				// 시작 시간부터 현재 시간까지 모든 시간대 생성
				LocalDateTime now = LocalDateTime.now();
				List<ReactionTrendDataResponse.TrendDataPoint> likeTrendDataList = new ArrayList<>();
				List<ReactionTrendDataResponse.TrendDataPoint> dislikeTrendDataList = new ArrayList<>();

				// 시간별 데이터 생성 (현재 시점의 카운트 값 사용)
				LocalDateTime current = startTime.withMinute(0).withSecond(0).withNano(0);

				while (!current.isAfter(now)) {
					String timeKey = current.toString().replace("T", " ") + ":00";
					Long likeCount = likeDataMap.getOrDefault(timeKey, 0L);
					Long dislikeCount = dislikeDataMap.getOrDefault(timeKey, 0L);

					likeTrendDataList.add(new ReactionTrendDataResponse.TrendDataPoint(current, likeCount));
					dislikeTrendDataList.add(new ReactionTrendDataResponse.TrendDataPoint(current, dislikeCount));

					// 마지막 시간이면 현재 시간도 추가
					if (current.withMinute(0)
						.withSecond(0)
						.withNano(0)
						.equals(now.withMinute(0).withSecond(0).withNano(0)) && now.getMinute() > 0) {
						likeTrendDataList.add(new ReactionTrendDataResponse.TrendDataPoint(now, likeCount));
						dislikeTrendDataList.add(new ReactionTrendDataResponse.TrendDataPoint(now, dislikeCount));
					}

					current = current.plusHours(1);
				}

				return ReactionTrendDataResponse.builder()
					.coinId(coinId)
					.baseAsset(baseAsset)
					.coinTicker(coinTicker)
					.likeData(likeTrendDataList)
					.dislikeData(dislikeTrendDataList)
					.build();
			})
			.toList();
	}

}