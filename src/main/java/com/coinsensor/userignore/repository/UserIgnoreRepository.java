package com.coinsensor.userignore.repository;

import com.coinsensor.userignore.entity.UserIgnore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserIgnoreRepository extends JpaRepository<UserIgnore, Long>, CustomUserIgnoreRepository {

    List<UserIgnore> findByUser_UserId(Long userId);

    List<UserIgnore> findByTargetUser_UserId(Long targetUserId);

    Optional<UserIgnore> findByUser_UserIdAndTargetUser_UserId(Long userId, Long targetUserId);
}