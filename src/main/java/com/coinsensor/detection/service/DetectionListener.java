package com.coinsensor.detection.service;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.coinsensor.event.OhlcvDataSavedEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetectionListener {
	private final DetectionService detectionService;

	@Async
	@EventListener
	public void executeDetectionAsync(OhlcvDataSavedEvent event) {
		try {
			log.info("[{}-{}] {} 탐지 시작", event.getExchangeName(), event.getExchangeType(), event.getTimeframeName());
			detectionService.detectByTimeframe(event.getTimeframeName(), event.getExchangeType());
		} catch (Exception e) {
			log.error("{} 탐지 실패", event.getTimeframeName(), e);
		}
	}
}
