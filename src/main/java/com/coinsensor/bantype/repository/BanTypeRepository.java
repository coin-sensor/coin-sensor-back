package com.coinsensor.bantype.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.bantype.entity.BanType;

public interface BanTypeRepository extends JpaRepository<BanType, Long> {

	Optional<BanType> findByBanTypeId(Long banTypeId);

	void deleteByBanTypeId(Long banTypeId);
}