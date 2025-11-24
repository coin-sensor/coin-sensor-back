package com.coinsensor.reaction.repository;

import static com.coinsensor.reaction.entity.QReactionCount.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.coinsensor.reaction.entity.ReactionCount;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReactionCountCustomRepositoryImpl implements ReactionCountCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ReactionCount> findByTargetTypeAndTargetIdAndReactionId(String targetType, Long targetId, Long reactionId) {
        ReactionCount result = queryFactory
            .selectFrom(reactionCount)
            .where(
                reactionCount.targetType.eq(targetType)
                    .and(reactionCount.targetId.eq(targetId))
                    .and(reactionCount.reaction.reactionId.eq(reactionId))
            )
            .fetchOne();
            
        return Optional.ofNullable(result);
    }
}