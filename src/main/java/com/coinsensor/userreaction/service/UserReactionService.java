package com.coinsensor.userreaction.service;

import java.util.List;

import com.coinsensor.userreaction.dto.request.UserReactionRequest;
import com.coinsensor.userreaction.dto.response.CoinReactionResponse;
import com.coinsensor.userreaction.dto.response.ReactionCountResponse;
import com.coinsensor.userreaction.dto.response.ReactionTrendDataResponse;

public interface UserReactionService {

    List<ReactionCountResponse> toggleReaction(String userUuid, UserReactionRequest request);

    List<ReactionCountResponse> getReactionCounts(String targetType, Long targetId);

    List<CoinReactionResponse> getTopLikedCoins(int days, int limit);

    List<CoinReactionResponse> getTopDislikedCoins(int days, int limit);

    List<ReactionTrendDataResponse> getReactionsTrendData(int days, int limit);
}