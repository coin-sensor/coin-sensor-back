package com.coinsensor.coinohlcv.controller;

import com.coinsensor.coinohlcv.dto.response.CoinOhlcvResponse;
import com.coinsensor.coinohlcv.service.CoinOhlcvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coins")
@RequiredArgsConstructor
public class CoinOhlcvController {
    
    private final CoinOhlcvService coinOhlcvService;
    
    @GetMapping("/{coinId}/ohlcv")
    public ResponseEntity<List<CoinOhlcvResponse>> getOhlcv(@PathVariable Long coinId) {
        return ResponseEntity.ok(coinOhlcvService.getOhlcvByCoinId(coinId));
    }
}
