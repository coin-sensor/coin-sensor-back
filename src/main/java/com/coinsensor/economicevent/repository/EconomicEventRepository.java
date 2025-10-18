package com.coinsensor.economicevent.repository;

import com.coinsensor.economicevent.entity.EconomicEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EconomicEventRepository extends JpaRepository<EconomicEvent, Long> {
}
