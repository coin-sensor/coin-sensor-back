package com.coinsensor.exchangecoin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;
import com.coinsensor.exchangecoin.service.ExchangeCoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exchange-coins")
@RequiredArgsConstructor
public class ExchangeCoinController {

	private final ExchangeCoinService exchangeCoinService;

	@GetMapping("/top")
	public ResponseEntity<List<TopBottomCoinResponse>> getTopCoins(
		@RequestParam(defaultValue = "spot") String exchangeType) {
		return ResponseEntity.ok(exchangeCoinService.getTopCoins(exchangeType));
	}

	@GetMapping("/bottom")
	public ResponseEntity<List<TopBottomCoinResponse>> getBottomCoins(
		@RequestParam(defaultValue = "spot") String exchangeType) {
		return ResponseEntity.ok(exchangeCoinService.getBottomCoins(exchangeType));
	}
}