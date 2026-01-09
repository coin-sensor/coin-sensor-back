package com.coinsensor.scheduler;

import java.util.Objects;

import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheEvictionScheduler {

	private final CacheManager cacheManager;

	@Scheduled(cron = "0 0 * * * *")
	public void evictDetectableExchangeCoinsCache() {
		try {
			Objects.requireNonNull(cacheManager.getCache("detectableExchangeCoins")).clear();
			log.info("detectableExchangeCoins 캐시 삭제 완료");
		} catch (Exception e) {
			log.error("detectableExchangeCoins 캐시 삭제 실패: {}", e.getMessage());
		}
	}

	@Scheduled(cron = "0 0 * * * *")
	public void evictCoinClickCache() {
		try {
			Objects.requireNonNull(cacheManager.getCache("topViewedCoins")).clear();
			Objects.requireNonNull(cacheManager.getCache("coinsTrendData")).clear();
			log.info("코인 클릭 캐시 삭제 완료");
		} catch (Exception e) {
			log.error("코인 클릭 캐시 삭제 실패: {}", e.getMessage());
		}
	}
}