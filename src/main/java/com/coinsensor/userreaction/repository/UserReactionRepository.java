package com.coinsensor.userreaction.repository;

import com.coinsensor.userreaction.entity.UserReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserReactionRepository extends JpaRepository<UserReaction, String> {
    List<UserReaction> findByTargetTableAndTargetId(String targetTable, Long targetId);
    Optional<UserReaction> findByUser_UserIdAndTargetTableAndTargetId(Long userId, String targetTable, Long targetId);
    void deleteByUser_UserIdAndTargetTableAndTargetId(Long userId, String targetTable, Long targetId);
}