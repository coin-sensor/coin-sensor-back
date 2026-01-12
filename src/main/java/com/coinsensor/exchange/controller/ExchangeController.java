package com.coinsensor.exchange.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.exchange.dto.response.ExchangeResponse;
import com.coinsensor.exchange.service.ExchangeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/exchanges")
@RequiredArgsConstructor
public class ExchangeController {

	private final ExchangeService exchangeService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<ExchangeResponse>>> getAllExchanges() {
		return ApiResponse.createSuccess(exchangeService.getAllExchanges());
	}
}
