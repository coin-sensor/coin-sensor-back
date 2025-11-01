package com.coinsensor.coin.controller;

import com.coinsensor.coin.dto.response.CoinResponse;
import com.coinsensor.coin.service.CoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coins")
@RequiredArgsConstructor
public class CoinController {
    
    private final CoinService coinService;
    
    @GetMapping
    public ResponseEntity<List<CoinResponse>> getAllCoins() {
        return ResponseEntity.ok(coinService.getAllCoins());
    }
    
    @GetMapping("/{coinId}")
    public ResponseEntity<CoinResponse> getCoinById(@PathVariable Long coinId) {
        return ResponseEntity.ok(coinService.getCoinById(coinId));
    }
}
