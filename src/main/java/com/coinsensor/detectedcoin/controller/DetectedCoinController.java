package com.coinsensor.detectedcoin.controller;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.service.DetectedCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coins")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DetectedCoinController {
    
    private final DetectedCoinService detectedCoinService;
    
    @GetMapping("/abnormal")
    public ResponseEntity<List<DetectedCoinResponse>> getAbnormalCoins() {
        return ResponseEntity.ok(detectedCoinService.getAbnormalCoins());
    }
    
    @GetMapping("/volatile")
    public ResponseEntity<List<DetectedCoinResponse>> getVolatileCoins() {
        return ResponseEntity.ok(detectedCoinService.getVolatileCoins());
    }
}
