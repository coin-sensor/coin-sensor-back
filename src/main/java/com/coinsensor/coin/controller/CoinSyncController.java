package com.coinsensor.coin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.scheduler.BinanceCoinScheduler;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/sync")
@RequiredArgsConstructor
public class CoinSyncController {

	private final BinanceCoinScheduler binanceCoinScheduler;

	@PostMapping("/binanceCoins")
	public ResponseEntity<String> syncBinanceCoins() {
		binanceCoinScheduler.syncBinanceCoins();
		return ResponseEntity.ok("바이낸스 코인 동기화 완료");
	}
}
