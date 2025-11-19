package com.coinsensor.favoritecoin.repository;

import static com.coinsensor.favoritecoin.entity.QFavoriteCoin.*;
import static com.coinsensor.user.entity.QUser.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.coinsensor.favoritecoin.dto.response.FavoriteCoinResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomFavoriteCoinRepositoryImpl implements CustomFavoriteCoinRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<FavoriteCoinResponse> findAllByUuid(String uuid) {
		return queryFactory
			.select(Projections.constructor(
				FavoriteCoinResponse.class,
				favoriteCoin.exchangeCoin.exchangeCoinId,
				favoriteCoin.exchangeCoin.coin.coinTicker,
				favoriteCoin.exchangeCoin.exchange.name,
				favoriteCoin.exchangeCoin.exchange.type
			))
			.from(favoriteCoin)
			.where(user.uuid.eq(uuid))
			.fetch();
	}

	@Override
	public boolean existsByUuidAndExchangeCoinId(String uuid, Long exchangeCoinId) {
		return queryFactory
			.selectOne()
			.from(favoriteCoin)
			.where(favoriteCoin.user.uuid.eq(uuid)
				.and(favoriteCoin.exchangeCoin.exchangeCoinId.eq(exchangeCoinId)))
			.fetchFirst() != null;
	}

	@Override
	public void deleteByUuidAndExchangeCoinId(String uuid, Long exchangeCoinId) {
		queryFactory
			.delete(favoriteCoin)
			.where(favoriteCoin.user.uuid.eq(uuid)
				.and(favoriteCoin.exchangeCoin.exchangeCoinId.eq(exchangeCoinId)))
			.execute();
	}
}