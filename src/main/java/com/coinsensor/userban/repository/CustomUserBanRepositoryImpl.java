package com.coinsensor.userban.repository;

import static com.coinsensor.user.entity.QUser.*;
import static com.coinsensor.userban.entity.QUserBan.*;

import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.coinsensor.userban.dto.response.UserBanResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomUserBanRepositoryImpl implements CustomUserBanRepository {
	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsActiveBanByUuid(String uuid) {
		Integer count = queryFactory
			.selectOne()
			.from(userBan)
			.join(userBan.user, user)
			.where(user.uuid.eq(uuid)
				.and(userBan.endTime.after(LocalDateTime.now())))
			.fetchFirst();

		return count != null;
	}

	@Override
	public UserBanResponse findActiveBanByUserId(Long userId) {
		return queryFactory
			.select(Projections.constructor(UserBanResponse.class,
				userBan.userBanId,
				userBan.user.userId,
				userBan.banType.banTypeId,
				userBan.banType.reason,
				userBan.startTime,
				userBan.endTime))
			.from(userBan)
			.where(userBan.user.userId.eq(userId)
				.and(userBan.endTime.after(LocalDateTime.now())))
			.fetchFirst();
	}

	@Override
	public UserBanResponse findActiveBanByUuid(String uuid) {
		return queryFactory
			.select(Projections.constructor(UserBanResponse.class,
				userBan.userBanId,
				userBan.user.userId,
				userBan.banType.banTypeId,
				userBan.banType.reason,
				userBan.startTime,
				userBan.endTime))
			.from(userBan)
			.join(userBan.user, user)
			.where(user.uuid.eq(uuid)
				.and(userBan.endTime.after(LocalDateTime.now())))
			.fetchFirst();
	}
}