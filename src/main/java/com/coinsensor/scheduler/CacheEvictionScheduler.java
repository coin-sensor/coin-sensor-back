package com.coinsensor.scheduler;

import java.util.ArrayList;
import java.util.List;
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

	@Scheduled(cron = "0 */5 * * * *")
	public void evictCoinClickCache() {
		List<String> cacheNames = List.of("topViewedCoins", "coinsTrendData", "topLikedCoins", "topDislikedCoins",
			"likeTrendData", "dislikeTrendData");

		List<String> evictSuccessCacheNames = new ArrayList<>();
		List<String> evictFailCacheNames = new ArrayList<>();

		for (String cacheName : cacheNames) {
			try {
				Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
				evictSuccessCacheNames.add(cacheName);
			} catch (Exception e) {
				evictFailCacheNames.add(cacheName);
			}
		}

		if (!evictSuccessCacheNames.isEmpty()) {
			log.info("캐시 삭제 완료: {}", evictSuccessCacheNames);
		}

		if (!evictFailCacheNames.isEmpty()) {
			log.info("캐시 삭제 실패: {}", evictFailCacheNames);
		}
	}

}