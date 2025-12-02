package com.coinsensor.detectedcoin.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomDetectedCoinRepositoryImpl implements CustomDetectedCoinRepository {
	private final JPAQueryFactory queryFactory;
}