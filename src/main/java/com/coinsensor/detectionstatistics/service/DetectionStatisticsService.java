package com.coinsensor.detectionstatistics.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.detectionstatistics.entity.DetectionStatistics;
import com.coinsensor.detectionstatistics.repository.DetectionStatisticsRepository;
import com.coinsensor.timeframe.entity.Timeframe;
import com.coinsensor.timeframe.repository.TimeframeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DetectionStatisticsService {

	private final DetectionStatisticsRepository detectionStatisticsRepository;
	private final DetectedCoinRepository detectedCoinRepository;
	private final TimeframeRepository timeframeRepository;

	@Scheduled(cron = "0 * * * * *") // 매분 00초에 실행
	@Transactional
	public void aggregateDetectionStatistics() {
		List<DetectionStatistics> detectionStatistics = new ArrayList<>();

		List<Timeframe> timeframes = timeframeRepository.findAll();

		for (Timeframe timeframe : timeframes) {
			List<DetectedCoin> detectedCoins = detectedCoinRepository.finAllByTimeframe(timeframe);

			if (!detectedCoins.isEmpty()) {
				Long count = (long)detectedCoins.size();
				Double avgVolatility = detectedCoins.stream()
					.mapToDouble(detectedCoin -> detectedCoin.getVolatility().doubleValue())
					.average()
					.orElse(0.0);
				Double avgVolume = detectedCoins.stream()
					.mapToDouble(DetectedCoin::getVolume)
					.average()
					.orElse(0.0);

				detectionStatistics.add(DetectionStatistics.of(timeframe, count, avgVolatility, avgVolume));
			}
		}

		detectionStatisticsRepository.saveAll(detectionStatistics);
		log.info("통계 저장 완료: {}건", detectionStatistics.size());
	}

	public List<DetectionStatistics> getStatisticsByPeriod(String timeframeLabel, LocalDateTime startTime,
		LocalDateTime endTime) {
		return detectionStatisticsRepository.findByTimeframeLabelAndCreatedAtBetween(timeframeLabel, startTime,
			endTime);
	}

	public List<DetectionStatistics> getRecentStatistics(int hours) {
		LocalDateTime startTime = LocalDateTime.now().minusHours(hours);
		return detectionStatisticsRepository.findRecentStatistics(startTime);
	}
}