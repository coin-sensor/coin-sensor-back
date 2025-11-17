package com.coinsensor.detectionstatistics.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.detectionstatistics.entity.DetectionStatistics;
import com.coinsensor.detectionstatistics.service.DetectionStatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/detection-statistics")
@RequiredArgsConstructor
public class DetectionStatisticsController {

	private final DetectionStatisticsService detectionStatisticsService;

	@GetMapping
	public ResponseEntity<List<DetectionStatistics>> getStatistics(
		@RequestParam String timeframe,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime
	) {
		List<DetectionStatistics> statistics = detectionStatisticsService.getStatisticsByPeriod(
			timeframe, startTime, endTime);
		return ResponseEntity.ok(statistics);
	}

	@GetMapping("/recent")
	public ResponseEntity<List<DetectionStatistics>> getRecentStatistics(
		@RequestParam(defaultValue = "24") int hours
	) {
		List<DetectionStatistics> statistics = detectionStatisticsService.getRecentStatistics(hours);
		return ResponseEntity.ok(statistics);
	}
}