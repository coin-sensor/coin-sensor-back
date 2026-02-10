package com.coinsensor.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.analysis.entity.Analysis;

public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

}