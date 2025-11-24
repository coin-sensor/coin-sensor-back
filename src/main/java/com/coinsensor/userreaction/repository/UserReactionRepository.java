package com.coinsensor.userreaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.reaction.entity.Reaction;
import com.coinsensor.user.entity.User;
import com.coinsensor.userreaction.entity.UserReaction;

public interface UserReactionRepository extends JpaRepository<UserReaction, Long>, UserReactionCustomRepository {

	List<UserReaction> findByTargetTypeAndTargetId(String targetType, Long targetId);

	Optional<UserReaction> findByUserAndTargetTypeAndTargetId(User user, String targetType, Long targetId);

}