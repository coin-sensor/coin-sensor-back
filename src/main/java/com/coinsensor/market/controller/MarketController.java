package com.coinsensor.market.controller;

import com.coinsensor.dto.MarketOverviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MarketController {
    
    @GetMapping("/overview")
    public ResponseEntity<MarketOverviewDto> getMarketOverview() {
        MarketOverviewDto overview = MarketOverviewDto.builder()
                .bitcoinPrice(BigDecimal.valueOf(45000))
                .bitcoinChangePercent(BigDecimal.valueOf(2.5))
                .fearGreedIndex(65)
                .fearGreedLabel("Greed")
                .longShortRatio(BigDecimal.valueOf(58.7))
                .kimchiPremium(BigDecimal.valueOf(2.3))
                .lastUpdated(LocalDateTime.now())
                .build();
                
        return ResponseEntity.ok(overview);
    }
}