package com.coinsensor.coin.controller;

import com.coinsensor.scheduler.BinanceCoinScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sync")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CoinSyncController {
    
    private final BinanceCoinScheduler binanceCoinScheduler;
    
    @PostMapping("/binance-coins")
    public ResponseEntity<String> syncBinanceCoins() {
        binanceCoinScheduler.syncBinanceCoins();
        return ResponseEntity.ok("바이낸스 코인 동기화 완료");
    }
}
