package com.coinsensor.targettable.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coinsensor.targettable.entity.TargetTable;

@Repository
public interface TargetTableRepository extends JpaRepository<TargetTable, Long> {

	Optional<TargetTable> findByName(String name);
}