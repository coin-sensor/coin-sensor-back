package com.coinsensor.userreaction.repository;

import static com.coinsensor.userreaction.entity.QUserReaction.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.coinsensor.user.entity.User;
import com.coinsensor.userreaction.entity.UserReaction;
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
}