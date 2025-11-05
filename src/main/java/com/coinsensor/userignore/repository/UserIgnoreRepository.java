package com.coinsensor.userignore.repository;

import com.coinsensor.userignore.entity.UserIgnore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserIgnoreRepository extends JpaRepository<UserIgnore, Long> {
    List<UserIgnore> findByUser_UserId(Long userId);
    List<UserIgnore> findByTargetUser_UserId(Long targetUserId);
}