package com.coinsensor.clickcoin.controller;

import com.coinsensor.clickcoin.service.ClickCoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/click-coins")
@RequiredArgsConstructor
public class ClickCoinController {
    
    private final ClickCoinService clickCoinService;
    
    @PostMapping("/{exchangeCoinId}")
    public ResponseEntity<Void> incrementClickCount(@RequestHeader String uuid, @PathVariable Long exchangeCoinId) {
        clickCoinService.incrementClickCount(uuid, exchangeCoinId);
        return ResponseEntity.ok().build();
    }
}