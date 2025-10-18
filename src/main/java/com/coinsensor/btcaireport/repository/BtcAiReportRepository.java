package com.coinsensor.btcaireport.repository;

import com.coinsensor.btcaireport.entity.BtcAiReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BtcAiReportRepository extends JpaRepository<BtcAiReport, Long> {
}
