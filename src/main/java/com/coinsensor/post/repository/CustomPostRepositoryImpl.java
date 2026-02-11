package com.coinsensor.post.repository;

import static com.coinsensor.category.entity.QCategory.*;
import static com.coinsensor.post.entity.QPost.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.coinsensor.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<Post> getPostsByCategoryName(String categoryName, Pageable pageable) {
		List<Post> content = queryFactory
			.selectFrom(post)
			.join(post.category, category).fetchJoin()
			.where(category.name.eq(categoryName).and(post.deletedAt.isNull()))
			.orderBy(post.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = queryFactory
			.select(post.count())
			.from(post)
			.join(post.category, category)
			.where(category.name.eq(categoryName).and(post.deletedAt.isNull()))
			.fetchOne();

		return new PageImpl<>(content, pageable, total != null ? total : 0L);
	}

	@Override
	public Optional<Post> getByPostId(Long postId) {
		Post result = queryFactory
			.selectFrom(post)
			.where(post.postId.eq(postId).and(post.deletedAt.isNull()))
			.fetchOne();

		return Optional.ofNullable(result);
	}
}