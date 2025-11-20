package com.coinsensor.userban.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.coinsensor.userban.entity.QUserBan.userBan;

@Repository
@RequiredArgsConstructor
public class CustomUserBanRepositoryImpl implements CustomUserBanRepository {
    
    private final JPAQueryFactory queryFactory;
    
    @Override
    public void deleteByUuidAndTargetUserId(String uuid, Long targetUserId) {
        queryFactory
            .delete(userBan)
            .where(userBan.user.uuid.eq(uuid)
                .and(userBan.targetUser.userId.eq(targetUserId)))
            .execute();
    }
}