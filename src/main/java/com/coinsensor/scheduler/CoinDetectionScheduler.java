package com.coinsensor.scheduler;

import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
import com.coinsensor.detectioncriteria.repository.DetectionCriteriaRepository;
import com.coinsensor.service.CoinDetectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinDetectionScheduler {
    
    private final CoinDetectionService coinDetectionService;
    
    @Scheduled(cron = "0 * * * * ?") // 매분 정각
    public void detect1mCoins() {
        executeDetectionAsync("1m");
    }
    
    @Scheduled(cron = "0 */5 * * * ?") // 5분마다
    public void detect5mCoins() {
        executeDetectionAsync("5m");
    }
    
    @Scheduled(cron = "0 */15 * * * ?") // 15분마다
    public void detect15mCoins() {
        executeDetectionAsync("15m");
    }
    
    @Scheduled(cron = "0 0 * * * ?") // 매시간 정각
    public void detect1hCoins() {
        executeDetectionAsync("1h");
    }
    
    @Scheduled(cron = "0 0 1/4 * * ?") // 01:00부터 4시간마다
    public void detect4hCoins() {
        executeDetectionAsync("4h");
    }
    
    @Scheduled(cron = "0 0 9 * * ?") // 매일 09:00
    public void detect1dCoins() {
        executeDetectionAsync("1d");
    }
    
    private void executeDetectionAsync(String timeframeLabel) {
        CompletableFuture.runAsync(() -> {
            try {
                log.info("{} 탐지 시작", timeframeLabel);
                coinDetectionService.detectByTimeframe(timeframeLabel);
            } catch (Exception e) {
                log.error("{} 탐지 실패", timeframeLabel, e);
            }
        });
    }
}
