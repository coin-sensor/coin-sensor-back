package com.coinsensor.reaction.repository;

import java.util.Optional;

import com.coinsensor.reaction.entity.ReactionCount;

public interface ReactionCountCustomRepository {

    Optional<ReactionCount> findByTargetTypeAndTargetIdAndReactionId(String targetType, Long targetId, Long reactionId);
}