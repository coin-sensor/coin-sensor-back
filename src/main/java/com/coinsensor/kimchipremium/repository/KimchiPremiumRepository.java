package com.coinsensor.kimchipremium.repository;

import com.coinsensor.kimchipremium.entity.KimchiPremium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KimchiPremiumRepository extends JpaRepository<KimchiPremium, Long> {
}
