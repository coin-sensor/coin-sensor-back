package com.coinsensor.common.config;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchange.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    private final ExchangeRepository exchangeRepository;
    
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
    }
}
