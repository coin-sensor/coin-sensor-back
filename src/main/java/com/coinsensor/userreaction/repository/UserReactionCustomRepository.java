package com.coinsensor.userreaction.repository;

import com.coinsensor.targettable.entity.TargetTable;
import com.coinsensor.user.entity.User;
import com.coinsensor.userreaction.entity.UserReaction;

import java.util.Optional;

public interface UserReactionCustomRepository {

    Optional<UserReaction> findByUserAndTargetTableAndTargetId(User user, TargetTable targetTable, Long targetId);
}