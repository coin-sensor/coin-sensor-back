package com.coinsensor.conditions.repository;

import static com.coinsensor.conditions.entity.QCondition.*;

import java.util.List;
import java.util.Optional;

import com.coinsensor.conditions.entity.Condition;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomConditionRepositoryImpl implements CustomConditionRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<List<Condition>> findByTimeframeName(String timeframeName) {
		return Optional.ofNullable(queryFactory
			.selectFrom(condition)
			.join(condition.timeframe).fetchJoin()
			.where(condition.timeframe.name.eq(timeframeName))
			.fetch());
	}
}