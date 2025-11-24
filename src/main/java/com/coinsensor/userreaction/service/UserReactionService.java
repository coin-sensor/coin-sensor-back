package com.coinsensor.userreaction.service;

import java.util.List;

import com.coinsensor.userreaction.dto.request.UserReactionRequest;
import com.coinsensor.userreaction.dto.response.ReactionCountResponse;

public interface UserReactionService {

    void toggleReaction(String userUuid, UserReactionRequest request);

    List<ReactionCountResponse> getReactionCounts(String targetType, Long targetId);
}