package com.coinsensor.postview.repository;

import static com.coinsensor.postview.entity.QPostView.*;

import org.springframework.stereotype.Repository;

import com.coinsensor.postview.entity.PostView;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomPostViewRepositoryImpl implements CustomPostViewRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public PostView findByPostIdAndUuid(Long postId, String uuid) {
		return queryFactory
			.selectFrom(postView)
			.where(
				postView.post.postId.eq(postId)
					.and(postView.user.uuid.eq(uuid))
			)
			.fetchOne();
	}
}