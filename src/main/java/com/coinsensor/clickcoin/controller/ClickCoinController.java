package com.coinsensor.clickcoin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.clickcoin.dto.response.CoinTrendDataResponse;
import com.coinsensor.clickcoin.dto.response.CoinViewCountResponse;
import com.coinsensor.clickcoin.service.ClickCoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clickCoins")
@RequiredArgsConstructor
public class ClickCoinController {

	private final ClickCoinService clickCoinService;

	@GetMapping("/top")
	public ResponseEntity<List<CoinViewCountResponse>> getTopViewedCoins(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "3") int limit) {
		return ResponseEntity.ok(clickCoinService.getTopViewedCoins(days, limit));
	}

	@GetMapping("/trend")
	public ResponseEntity<List<CoinTrendDataResponse>> getCoinsTrendData(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "10") int limit) {
		return ResponseEntity.ok(clickCoinService.getCoinsTrendData(days, limit));
	}

}