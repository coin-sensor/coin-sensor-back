package com.coinsensor.userban.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.userban.entity.UserBan;

public interface UserBanRepository extends JpaRepository<UserBan, Long>, CustomUserBanRepository {

	List<UserBan> findByUserUserId(Long userId);

	void deleteByUserBanId(Long userBanId);
}