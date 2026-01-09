package com.coinsensor.userreaction.repository;

import com.coinsensor.user.entity.User;
import com.coinsensor.userreaction.dto.response.CoinReactionResponse;
import com.coinsensor.userreaction.dto.response.ReactionTrendDataResponse;
import com.coinsensor.userreaction.entity.UserReaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserReactionCustomRepository {

    Optional<UserReaction> findByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);

    List<CoinReactionResponse> findTopLikedCoins(LocalDateTime startTime, int limit);

    List<CoinReactionResponse> findTopDislikedCoins(LocalDateTime startTime, int limit);

    List<ReactionTrendDataResponse> findLikeTrendData(LocalDateTime startTime, int limit);

    List<ReactionTrendDataResponse> findDislikeTrendData(LocalDateTime startTime, int limit);
}