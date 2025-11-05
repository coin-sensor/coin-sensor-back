package com.coinsensor.bantype.repository;

import com.coinsensor.bantype.entity.BanType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BanTypeRepository extends JpaRepository<BanType, Long> {
}