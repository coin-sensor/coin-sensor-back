package com.coinsensor.scheduler;

import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
import com.coinsensor.detectioncriteria.repository.DetectionCriteriaRepository;
import com.coinsensor.service.CoinDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinDetectionScheduler {
    
    private final CoinDetectionService coinDetectionService;
    
    @Scheduled(cron = "0 * * * * ?") // 매분 정각
    public void detect1mCoins() {
        executeDetection("1m");
    }
    
    @Scheduled(cron = "0 */5 * * * ?") // 5분마다
    public void detect5mCoins() {
        executeDetection("5m");
    }
    
    @Scheduled(cron = "0 */15 * * * ?") // 15분마다
    public void detect15mCoins() {
        executeDetection("15m");
    }
    
    @Scheduled(cron = "0 0 * * * ?") // 매시간 정각
    public void detect1hCoins() {
        executeDetection("1h");
    }
    
    @Scheduled(cron = "0 0 */4 * * ?") // 4시간마다
    public void detect4hCoins() {
        executeDetection("4h");
    }
    
    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정
    public void detect1dCoins() {
        executeDetection("1d");
    }
    
    private void executeDetection(String timeframeLabel) {
        coinDetectionService.detectByTimeframe(timeframeLabel);
    }
}
