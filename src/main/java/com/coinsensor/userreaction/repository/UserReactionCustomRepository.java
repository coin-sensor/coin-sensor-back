package com.coinsensor.userreaction.repository;

import com.coinsensor.user.entity.User;
import com.coinsensor.userreaction.entity.UserReaction;

import java.util.Optional;

public interface UserReactionCustomRepository {

    Optional<UserReaction> findByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);
}