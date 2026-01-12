package com.coinsensor.coinclick.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.coinclick.dto.response.CoinTrendDataResponse;
import com.coinsensor.coinclick.dto.response.CoinViewCountResponse;
import com.coinsensor.coinclick.service.CoinClickService;
import com.coinsensor.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coinClicks")
@RequiredArgsConstructor
public class CoinClickController {

	private final CoinClickService coinClickService;

	@GetMapping("/top")
	public ResponseEntity<ApiResponse<List<CoinViewCountResponse>>> getTopViewedCoins(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "3") int limit) {
		return ApiResponse.createSuccess(coinClickService.getTopViewedCoins(days, limit));
	}

	@GetMapping("/trend")
	public ResponseEntity<ApiResponse<List<CoinTrendDataResponse>>> getCoinsTrendData(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "10") int limit) {
		return ApiResponse.createSuccess(coinClickService.getCoinsTrendData(days, limit));
	}

}