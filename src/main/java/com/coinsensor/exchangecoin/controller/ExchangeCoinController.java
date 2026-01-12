package com.coinsensor.exchangecoin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.annotation.AuthorizeRole;
import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.exchangecoin.dto.response.ExchangeCoinResponse;
import com.coinsensor.exchangecoin.dto.response.TopBottomCoinResponse;
import com.coinsensor.exchangecoin.service.ExchangeCoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exchangeCoins")
@RequiredArgsConstructor
public class ExchangeCoinController {

	private final ExchangeCoinService exchangeCoinService;

	@GetMapping("/top")
	public ResponseEntity<ApiResponse<List<TopBottomCoinResponse>>> getTopCoins(
		@RequestParam(defaultValue = "spot") String exchangeType) {
		return ApiResponse.createSuccess(exchangeCoinService.getTopCoins(exchangeType));
	}

	@GetMapping("/bottom")
	public ResponseEntity<ApiResponse<List<TopBottomCoinResponse>>> getBottomCoins(
		@RequestParam(defaultValue = "spot") String exchangeType) {
		return ApiResponse.createSuccess(exchangeCoinService.getBottomCoins(exchangeType));
	}

	@AuthorizeRole
	@GetMapping
	public ResponseEntity<ApiResponse<List<ExchangeCoinResponse>>> getExchangeCoins() {
		return ApiResponse.createSuccess(exchangeCoinService.getExchangeCoins());
	}

	@AuthorizeRole
	@PutMapping("/{exchangeCoinId}/detection/toggle")
	public ResponseEntity<ApiResponse<Void>> updateExchangeCoinEnableDetection(
		@PathVariable Long exchangeCoinId) {
		exchangeCoinService.updateExchangeCoinEnableDetection(exchangeCoinId);
		return ResponseEntity.ok().build();
	}
}