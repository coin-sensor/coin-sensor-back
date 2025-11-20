package com.coinsensor.userban.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CustomUserBanRepositoryImpl implements CustomUserBanRepository {
	private final JPAQueryFactory queryFactory;

}