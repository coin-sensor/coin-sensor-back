package com.coinsensor.detectedcoin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.clickcoin.service.ClickCoinService;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.service.DetectedCoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coins")
@RequiredArgsConstructor
public class DetectedCoinController {

	private final DetectedCoinService detectedCoinService;
	private final ClickCoinService clickCoinService;

	@GetMapping("/abnormal")
	public ResponseEntity<List<DetectedCoinResponse>> getAbnormalCoins() {
		return ResponseEntity.ok(detectedCoinService.getAbnormalCoins());
	}

	@GetMapping("/volatile")
	public ResponseEntity<List<DetectedCoinResponse>> getVolatileCoins() {
		return ResponseEntity.ok(detectedCoinService.getVolatileCoins());
	}

	@GetMapping("/detected")
	public ResponseEntity<List<DetectedCoinResponse>> getDetectedCoins(
		@RequestParam String exchangeName, @RequestParam String exchangeType) {
		return ResponseEntity.ok(detectedCoinService.getDetectedCoinsByTimeAndType(exchangeName, exchangeType));
	}

	@PostMapping("/detected/{detectedCoinId}/view")
	public ResponseEntity<Void> viewDetectedCoin(@RequestHeader String uuid, @PathVariable Long detectedCoinId) {
		detectedCoinService.viewDetectedCoin(uuid, detectedCoinId);
		return ResponseEntity.ok().build();
	}
}
