package com.coinsensor.timeframe.repository;

import com.coinsensor.timeframe.entity.Timeframe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeframeRepository extends JpaRepository<Timeframe, Long> {
    java.util.Optional<Timeframe> findByName(String name);
}
