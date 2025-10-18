package com.coinsensor.exchange.controller;

import com.coinsensor.exchange.dto.response.ExchangeResponse;
import com.coinsensor.exchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/exchanges")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ExchangeController {
    
    private final ExchangeService exchangeService;
    
    @GetMapping
    public ResponseEntity<List<ExchangeResponse>> getAllExchanges() {
        return ResponseEntity.ok(exchangeService.getAllExchanges());
    }
}
