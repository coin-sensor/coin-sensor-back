package com.coinsensor.clickcoin.repository;

import static com.coinsensor.clickcoin.entity.QClickCoin.*;

import java.util.Optional;

import com.coinsensor.clickcoin.entity.ClickCoin;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomClickCoinRepositoryImpl implements CustomClickCoinRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<ClickCoin> findByUuidAndDetectedCoinId(String uuid, Long detectedCoinId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(clickCoin)
				.where(clickCoin.user.uuid.eq(uuid)
					.and(clickCoin.detectedCoin.detectedCoinId.eq(detectedCoinId)))
				.fetchOne());
	}
}