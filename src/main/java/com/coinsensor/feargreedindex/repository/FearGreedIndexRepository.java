package com.coinsensor.feargreedindex.repository;

import com.coinsensor.feargreedindex.entity.FearGreedIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FearGreedIndexRepository extends JpaRepository<FearGreedIndex, Long> {
}
