package com.coinsensor.ohlcvs.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.event.OhlcvDataSavedEvent;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.service.ExchangeCoinService;
import com.coinsensor.ohlcvs.entity.Ohlcv;
import com.coinsensor.ohlcvs.repository.OhlcvRepository;
import com.coinsensor.timeframe.entity.Timeframe;
import com.coinsensor.timeframe.repository.TimeframeRepository;
import com.coinsensor.websocket.dto.KlineData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OhlcvServiceImpl implements OhlcvService {

	private static final String BINANCE = "binance";
	private final OhlcvRepository ohlcvRepository;
	private final ExchangeCoinService exchangeCoinService;
	private final TimeframeRepository timeframeRepository;
	private final ApplicationEventPublisher eventPublisher;

	@Override
	public void saveKlineData(List<KlineData> klineDataList, Exchange.Type exchangeType) {
		String timeframeName = klineDataList.getFirst().getKline().getInterval();

		try {
			if (klineDataList.isEmpty())
				return;

			// Timeframe 미리 조회 (모든 데이터가 같은 timeframe)
			Timeframe timeframe = timeframeRepository.findByName(timeframeName).orElse(null);
			if (timeframe == null)
				return;

			// ExchangeCoin 일괄 조회
			List<ExchangeCoin> exchangeCoins = exchangeCoinService
				.getDetectableExchangeCoins(BINANCE, exchangeType);

			Map<String, ExchangeCoin> coinMap = exchangeCoins.stream()
				.collect(Collectors.toMap(
					ec -> ec.getCoin().getCoinTicker(),
					ec -> ec
				));

			List<Ohlcv> ohlcvList = new ArrayList<>();
			for (KlineData klineData : klineDataList) {
				ExchangeCoin exchangeCoin = coinMap.get(klineData.getSymbol());
				if (exchangeCoin == null)
					continue;

				KlineData.KlineInfo kline = klineData.getKline();
				Ohlcv ohlcv = Ohlcv.from(kline, exchangeCoin, timeframe);
				ohlcvList.add(ohlcv);
			}

			if (!ohlcvList.isEmpty()) {
				ohlcvRepository.saveAll(ohlcvList);
				log.info("[{}-{}] {} OHLCV 배치 저장 완료: {} 건", BINANCE, exchangeType, timeframeName, ohlcvList.size());

				// 이벤트 발행
				eventPublisher.publishEvent(
					new OhlcvDataSavedEvent(timeframeName, BINANCE, exchangeType, ohlcvList.size()));
			}
		} catch (Exception e) {
			log.error("[{}-{}] {} OHLCV 배치 저장 오류: {}", BINANCE, exchangeType, timeframeName, e.getMessage());
		}
	}

	@Override
	public long cleanupOldData(int years) {
		LocalDateTime cutoffDate = LocalDateTime.now().minusYears(years);
		return ohlcvRepository.deleteByCreatedAtBefore(cutoffDate);
	}
}