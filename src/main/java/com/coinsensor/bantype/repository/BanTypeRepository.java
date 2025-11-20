package com.coinsensor.bantype.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.bantype.entity.BanType;

public interface BanTypeRepository extends JpaRepository<BanType, Long> {

	void deleteByBanTypeId(Long banTypeId);
}