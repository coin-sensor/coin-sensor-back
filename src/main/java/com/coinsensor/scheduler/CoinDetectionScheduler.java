// package com.coinsensor.scheduler;
//
// import java.util.concurrent.CompletableFuture;
//
// import org.springframework.context.event.EventListener;
// import org.springframework.scheduling.annotation.Async;
// import org.springframework.scheduling.annotation.Scheduled;
// import org.springframework.stereotype.Component;
//
// import com.coinsensor.detection.service.DetectionService;
// import com.coinsensor.event.OhlcvDataSavedEvent;
//
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Component
// @RequiredArgsConstructor
// @Deprecated(since = "Listener 기반 방식 변경 후 미사용 처리")
// public class CoinDetectionScheduler {
// 	private final DetectionService detectionService;
//
// 	@Scheduled(cron = "0 * * * * ?") // 매분 정각
// 	public void detect1mCoins() {
// 		executeDetectionAsync("1m");
// 	}
//
// 	@Scheduled(cron = "0 */5 * * * ?") // 5분마다
// 	public void detect5mCoins() {
// 		executeDetectionAsync("5m");
// 	}
//
// 	@Scheduled(cron = "0 */15 * * * ?") // 15분마다
// 	public void detect15mCoins() {
// 		executeDetectionAsync("15m");
// 	}
//
// 	@Scheduled(cron = "0 0 * * * ?") // 매시간 정각
// 	public void detect1hCoins() {
// 		executeDetectionAsync("1h");
// 	}
//
// 	@Scheduled(cron = "0 0 1/4 * * ?") // 01:00부터 4시간마다
// 	public void detect4hCoins() {
// 		executeDetectionAsync("4h");
// 	}
//
// 	@Scheduled(cron = "0 0 9 * * ?") // 매일 09:00
// 	public void detect1dCoins() {
// 		executeDetectionAsync("1d");
// 	}
//
// 	private void executeDetectionAsync(String timeframeName) {
// 		CompletableFuture.runAsync(() -> {
// 			try {
// 				// 5초 대기 후 탐지 실행
// 				Thread.sleep(5000);
// 				log.info("{} 탐지 시작", timeframeName);
// 				detectionService.detectByTimeframe(timeframeName);
// 			} catch (InterruptedException e) {
// 				Thread.currentThread().interrupt();
// 				log.warn("{} 탐지 인터럽트", timeframeName);
// 			} catch (Exception e) {
// 				log.error("{} 탐지 실패", timeframeName, e);
// 			}
// 		});
// 	}
//
//
// }
