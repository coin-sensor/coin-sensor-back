package com.coinsensor.reaction.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.reaction.entity.ReactionCount;

public interface ReactionCountRepository extends JpaRepository<ReactionCount, Long>, ReactionCountCustomRepository {

    List<ReactionCount> findByTargetTypeAndTargetId(String targetType, Long targetId);
}