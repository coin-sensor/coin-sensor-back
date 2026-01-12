package com.coinsensor.coin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.coin.dto.response.CoinResponse;
import com.coinsensor.coin.service.CoinService;
import com.coinsensor.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coins")
@RequiredArgsConstructor
public class CoinController {

	private final CoinService coinService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<CoinResponse>>> getAllCoins() {
		return ApiResponse.createSuccess(coinService.getAllCoins());
	}

	@GetMapping("/{coinId}")
	public ResponseEntity<ApiResponse<CoinResponse>> getCoinById(@PathVariable Long coinId) {
		return ApiResponse.createSuccess(coinService.getCoinById(coinId));
	}
}
