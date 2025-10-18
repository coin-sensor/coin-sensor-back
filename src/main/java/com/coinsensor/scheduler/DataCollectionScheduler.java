package com.coinsensor.scheduler;

import com.coinsensor.dto.CoinDataDto;
import com.coinsensor.external.binance.BinanceApiClient;

import com.coinsensor.websocket.WebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataCollectionScheduler {
    
    private final BinanceApiClient binanceApiClient;
    private final WebSocketHandler webSocketHandler;
    
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void collectCoinData() {
        log.info("Starting coin data collection...");
        
        binanceApiClient.get24hrTickers()
                .subscribe(
                    tickers -> {
                        processTickers(tickers);
                        log.info("Processed {} tickers", tickers.size());
                    },
                    error -> log.error("Failed to collect coin data", error)
                );
    }
    
    @Scheduled(fixedRate = 300000) // 5분마다 실행
    public void collectMarketIndicators() {
        log.info("Starting market indicators collection...");
        
        // TODO: 공포탐욕지수, 롱숏비율, 김치프리미엄 수집
        // 현재는 더미 데이터로 대체
        
        log.info("Market indicators collection completed");
    }
    
    @Scheduled(cron = "0 0 1 * * ?") // 매일 새벽 1시 실행
    public void dailyDataCleanup() {
        log.info("Starting daily data cleanup...");
        
        // TODO: 오래된 데이터 정리 로직 구현
        
        log.info("Daily data cleanup completed");
    }
    
    private void processTickers(List<CoinDataDto> tickers) {
        for (CoinDataDto ticker : tickers) {
            try {
                // 데이터베이스 업데이트
                updateCoinInDatabase(ticker);
                
                // 실시간 데이터 처리

                
                // WebSocket으로 브로드캐스트
                webSocketHandler.broadcastCoinData(ticker);
                
            } catch (Exception e) {
                log.warn("Failed to process ticker for {}: {}", ticker.getSymbol(), e.getMessage());
            }
        }
    }
    
    private void updateCoinInDatabase(CoinDataDto ticker) {

    }
}