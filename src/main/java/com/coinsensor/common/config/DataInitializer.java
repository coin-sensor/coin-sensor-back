package com.coinsensor.common.config;

import com.coinsensor.chatroom.entity.ChatRoom;
import com.coinsensor.chatroom.repository.ChatRoomRepository;
import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
import com.coinsensor.detectioncriteria.repository.DetectionCriteriaRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchange.repository.ExchangeRepository;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import com.coinsensor.scheduler.BinanceCoinScheduler;
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
    private final ExchangeCoinRepository exchangeCoinRepository;
    private final BinanceCoinScheduler binanceCoinScheduler;
    private final ChatRoomRepository chatRoomRepository;

    
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initData() {
        if (exchangeRepository.count() == 0) {
            exchangeRepository.save(new Exchange("binance", Exchange.Type.spot));
            exchangeRepository.save(new Exchange("binance", Exchange.Type.future));
            exchangeRepository.save(new Exchange("upbit", Exchange.Type.spot));
            exchangeRepository.save(new Exchange("bithumb", Exchange.Type.spot));

            log.info("초기 거래소 데이터 생성 완료");
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

        if (exchangeCoinRepository.count() == 0) {
            binanceCoinScheduler.syncBinanceCoins();

            log.info("초기 바이낸스 코인 데이터 생성 완료");
        }

        if (chatRoomRepository.count() == 0) {
            chatRoomRepository.save(new ChatRoom("일반"));

            log.info("초기 채팅방 데이터 생성 완료");
        }
    }
}
