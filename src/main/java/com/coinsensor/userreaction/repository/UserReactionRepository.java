package com.coinsensor.userreaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.reaction.entity.Reaction;
import com.coinsensor.targettable.entity.TargetTable;
import com.coinsensor.user.entity.User;
import com.coinsensor.userreaction.entity.UserReaction;

public interface UserReactionRepository extends JpaRepository<UserReaction, Long>, UserReactionCustomRepository {

	List<UserReaction> findByTargetTableAndTargetId(TargetTable table, Long targetId);

	Optional<UserReaction> findByUserAndReactionAndTargetTableAndTargetId(User user, Reaction reaction,
		TargetTable table, Long targetId);

}