package com.coinsensor.detectedcoin.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.detectedcoin.service.DetectedCoinService;
import com.coinsensor.detection.dto.response.TopDetectedCoinResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/detectedCoins")
@RequiredArgsConstructor
public class DetectedCoinController {

	private final DetectedCoinService detectedCoinService;

	@PostMapping("/{detectedCoinId}/view")
	public ResponseEntity<ApiResponse<Long>> viewDetectedCoin(@RequestHeader String uuid,
		@PathVariable Long detectedCoinId) {
		Long viewCount = detectedCoinService.viewDetectedCoin(uuid, detectedCoinId);
		return ApiResponse.createSuccess(viewCount);
	}

	@GetMapping("/top10")
	public ResponseEntity<ApiResponse<List<TopDetectedCoinResponse>>> getTopDetectedCoins(
		@RequestParam String timeframe,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
		return ApiResponse.createSuccess(detectedCoinService.getTopDetectedCoins(timeframe, startTime, endTime));
	}
}