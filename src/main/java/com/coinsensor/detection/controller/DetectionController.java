package com.coinsensor.detection.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.service.DetectionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/detections")
@RequiredArgsConstructor
public class DetectionController {

	private final DetectionService detectionService;

	@GetMapping
	public ResponseEntity<List<DetectionInfoResponse>> getDetections(
		@RequestParam String exchange,
		@RequestParam String exchangeType,
		@RequestParam String coinRanking,
		@RequestParam String timeframe) {
		return ResponseEntity.ok(detectionService.getDetections(exchange, exchangeType, coinRanking, timeframe));
	}
}