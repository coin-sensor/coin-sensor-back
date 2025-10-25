package com.coinsensor.common.config;

import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
import com.coinsensor.detectioncriteria.repository.DetectionCriteriaRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchange.repository.ExchangeRepository;
import com.coinsensor.timeframe.entity.Timeframe;
import com.coinsensor.timeframe.repository.TimeframeRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    private final ExchangeRepository exchangeRepository;
    private final TimeframeRepository timeframeRepository;
    private final DetectionCriteriaRepository detectionCriteriaRepository;

    
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initData() {
        if (!exchangeRepository.existsByName("binance")) {
            Exchange binance = Exchange.builder()
                    .name("binance")
                    .build();
            exchangeRepository.save(binance);
            log.info("초기 거래소 데이터 생성: binance");
        }
        
        if (timeframeRepository.count() == 0) {
            List<String> timeframes = List.of("1m", "5m", "15m", "1h", "4h", "1d");
            for (String tf : timeframes) {
                timeframeRepository.save(new Timeframe(tf));
            }
            log.info("초기 타임프레임 데이터 생성: {}", timeframes);
        }
        
        if (detectionCriteriaRepository.count() == 0) {
            Timeframe tf1m = timeframeRepository.findByTimeframeLabel("1m").orElseThrow();
            Timeframe tf5m = timeframeRepository.findByTimeframeLabel("5m").orElseThrow();
            Timeframe tf1h = timeframeRepository.findByTimeframeLabel("1h").orElseThrow();
            Timeframe tf4h = timeframeRepository.findByTimeframeLabel("4h").orElseThrow();

            detectionCriteriaRepository.save(new DetectionCriteria(tf1m, BigDecimal.valueOf(1.00), 2.0));
            detectionCriteriaRepository.save(new DetectionCriteria(tf5m, BigDecimal.valueOf(1.00), 2.0));
            detectionCriteriaRepository.save(new DetectionCriteria(tf1h, BigDecimal.valueOf(3.00), 2.0));
            detectionCriteriaRepository.save(new DetectionCriteria(tf4h, BigDecimal.valueOf(5.00), 2.0));
            
            log.info("초기 감지 기준 데이터 생성 완료");
        }
    }
}
