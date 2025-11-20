package com.coinsensor.userignore.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.coinsensor.userignore.entity.QUserIgnore.userIgnore;
import static com.coinsensor.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CustomUserIgnoreRepositoryImpl implements CustomUserIgnoreRepository {
    
    private final JPAQueryFactory queryFactory;
    
    @Override
    public void deleteByUuidAndTargetUserId(String uuid, Long targetUserId) {
        queryFactory
            .delete(userIgnore)
            .where(userIgnore.user.uuid.eq(uuid)
                .and(userIgnore.targetUser.userId.eq(targetUserId)))
            .execute();
    }
}