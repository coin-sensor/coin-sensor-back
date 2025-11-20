package com.coinsensor.userban.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.userban.entity.UserBan;

public interface UserBanRepository extends JpaRepository<UserBan, Long>, CustomUserBanRepository {

	void deleteByUserBanId(Long userBanId);
}