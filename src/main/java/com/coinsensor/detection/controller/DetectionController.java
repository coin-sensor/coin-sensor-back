package com.coinsensor.detection.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.detection.dto.response.DetectionChartResponse;
import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.service.DetectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/detections")
@RequiredArgsConstructor
public class DetectionController {

	private final DetectionService detectionService;

	@GetMapping
	public ResponseEntity<List<DetectionInfoResponse>> getDetectionInfos(
		@RequestParam String exchange,
		@RequestParam String exchangeType,
		@RequestParam String coinCategory,
		@RequestParam String timeframe) {
		return ResponseEntity.ok(detectionService.getDetectionInfos(exchange, exchangeType, coinCategory, timeframe));
	}

	@GetMapping("/chart")
	public ResponseEntity<DetectionChartResponse> getDetectionChart(
		@RequestParam String timeframe,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
		return ResponseEntity.ok(detectionService.getDetectionChart(timeframe, startTime, endTime));
	}
}