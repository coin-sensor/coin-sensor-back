package com.coinsensor.banuser.repository;

import com.coinsensor.banuser.entity.BanUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface BanUserRepository extends JpaRepository<BanUser, Long> {
    List<BanUser> findByUser_UserIdAndEndTimeAfter(Long userId, LocalDateTime currentTime);
}