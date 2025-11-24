package com.coinsensor.userreaction.dto.response;

import com.coinsensor.reaction.entity.ReactionCount;

public record ReactionCountResponse(
    Long reactionId,
    String reactionName,
    Long count
) {
    public static ReactionCountResponse from(ReactionCount reactionCount) {
        return new ReactionCountResponse(
            reactionCount.getReaction().getReactionId(),
            reactionCount.getReaction().getName(),
            reactionCount.getCount()
        );
    }
}