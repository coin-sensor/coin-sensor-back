package com.coinsensor.common.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.bantype.entity.BanType;
import com.coinsensor.bantype.repository.BanTypeRepository;
import com.coinsensor.channel.entity.Channel;
import com.coinsensor.channel.repository.ChannelRepository;
import com.coinsensor.common.annotation.LeaderOnly;
import com.coinsensor.conditions.entity.Condition;
import com.coinsensor.conditions.repository.ConditionRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchange.repository.ExchangeRepository;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import com.coinsensor.reaction.entity.Reaction;
import com.coinsensor.reaction.repository.ReactionRepository;
import com.coinsensor.scheduler.BinanceCoinScheduler;
import com.coinsensor.timeframe.entity.Timeframe;
import com.coinsensor.timeframe.repository.TimeframeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

	private final ExchangeRepository exchangeRepository;
	private final TimeframeRepository timeframeRepository;
	private final ConditionRepository conditionRepository;
	private final ExchangeCoinRepository exchangeCoinRepository;
	private final BinanceCoinScheduler binanceCoinScheduler;
	private final ChannelRepository channelRepository;
	private final ReactionRepository reactionRepository;
	private final BanTypeRepository banTypeRepository;
	private final org.springframework.context.ApplicationContext applicationContext;

	@LeaderOnly
	@EventListener(ApplicationReadyEvent.class)
	@Transactional
	public void initData() {

		if (exchangeRepository.count() != 0) {
			log.info("[ ** 초기 데이터 생성을 Skip ** ]");
		} else {
			log.info("[ ** 초기 데이터 생성 시작 ** ]");

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

		if (conditionRepository.count() == 0) {
			Timeframe tf1m = timeframeRepository.findByName("1m").orElseThrow();
			Timeframe tf5m = timeframeRepository.findByName("5m").orElseThrow();
			Timeframe tf15m = timeframeRepository.findByName("15m").orElseThrow();
			Timeframe tf1h = timeframeRepository.findByName("1h").orElseThrow();
			Timeframe tf4h = timeframeRepository.findByName("4h").orElseThrow();
			Timeframe tf1d = timeframeRepository.findByName("1d").orElseThrow();

			conditionRepository.save(new Condition(tf1m, BigDecimal.valueOf(1.00), BigDecimal.valueOf(2.00)));
			conditionRepository.save(new Condition(tf5m, BigDecimal.valueOf(1.00), BigDecimal.valueOf(2.00)));
			conditionRepository.save(new Condition(tf15m, BigDecimal.valueOf(2.00), BigDecimal.valueOf(2.00)));
			conditionRepository.save(new Condition(tf1h, BigDecimal.valueOf(3.00), BigDecimal.valueOf(2.00)));
			conditionRepository.save(new Condition(tf4h, BigDecimal.valueOf(5.00), BigDecimal.valueOf(2.00)));
			conditionRepository.save(new Condition(tf4h, BigDecimal.valueOf(10.00), BigDecimal.valueOf(2.00)));

			log.info("초기 감지 조건 데이터 생성 완료");
		}

		if (exchangeCoinRepository.count() == 0) {
			binanceCoinScheduler.syncBinanceCoins();

			log.info("초기 바이낸스 코인 데이터 생성 완료");
		}

		if (channelRepository.count() == 0) {
			channelRepository.save(new Channel("일반"));

			log.info("초기 채팅방 데이터 생성 완료");
		}

		if (reactionRepository.count() == 0) {
			reactionRepository.save(new Reaction("like"));
			reactionRepository.save(new Reaction("dislike"));
		}

		if (banTypeRepository.count() == 0) {
			banTypeRepository.save(new BanType("스팸/도배", 1L));
			banTypeRepository.save(new BanType("기타", 3L));
			banTypeRepository.save(new BanType("욕설/비방", 7L));
			banTypeRepository.save(new BanType("허위정보 유포", 30L));
			banTypeRepository.save(new BanType("광고/홍보", 90L));
			banTypeRepository.save(new BanType("사기/피싱", 365L));
			banTypeRepository.save(new BanType("심각한 규정 위반", 36500L));

			log.info("초기 차단 유형 데이터 생성 완료");
		}

		log.info("[ ** 초기 데이터 생성 완료 ** ]");

		com.coinsensor.websocket.service.BinanceKlineWebSocketService webSocketService =
			applicationContext.getBean(com.coinsensor.websocket.service.BinanceKlineWebSocketService.class);
		webSocketService.initWebSocketConnections();
	}
}