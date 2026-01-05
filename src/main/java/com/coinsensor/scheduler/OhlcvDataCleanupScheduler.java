package com.coinsensor.scheduler;

import com.coinsensor.ohlcvs.service.OhlcvService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OhlcvDataCleanupScheduler {
    
    private final OhlcvService ohlcvService;
    
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupOldOhlcvData() {
        log.info("1년 이상 된 OHLCV 데이터 정리 시작");
        
        try {
            long deletedCount = ohlcvService.cleanupOldData(1);
            log.info("1년 이상 된 OHLCV 데이터 정리 완료: {} 건 삭제", deletedCount);
            
        } catch (Exception e) {
            log.error("OHLCV 데이터 정리 중 오류 발생", e);
        }
    }
}