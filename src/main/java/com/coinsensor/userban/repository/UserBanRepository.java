package com.coinsensor.userban.repository;

import com.coinsensor.userban.entity.UserBan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserBanRepository extends JpaRepository<UserBan, Long> {
    List<UserBan> findByUser_UserId(Long userId);
    List<UserBan> findByTargetUser_UserId(Long targetUserId);
}