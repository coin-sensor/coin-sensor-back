package com.coinsensor.ohlcvs.controller;

import com.coinsensor.ohlcvs.dto.response.OhlcvResponse;
import com.coinsensor.ohlcvs.service.OhlcvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ohlcvs")
@RequiredArgsConstructor
public class OhlcvController {
    
    private final OhlcvService ohlcvService;
    
    @GetMapping("/exchange-coin/{exchangeCoinId}")
    public ResponseEntity<List<OhlcvResponse>> getOhlcv(@PathVariable Long exchangeCoinId) {
        return ResponseEntity.ok(ohlcvService.getOhlcvByExchangeCoinId(exchangeCoinId));
    }
}