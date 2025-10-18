package com.coinsensor.kimchipremium.controller;

import com.coinsensor.kimchipremium.dto.response.KimchiPremiumResponse;
import com.coinsensor.kimchipremium.service.KimchiPremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class KimchiPremiumController {
    
    private final KimchiPremiumService kimchiPremiumService;
    
    @GetMapping("/kimchi-premium")
    public ResponseEntity<KimchiPremiumResponse> getLatestKimchiPremium() {
        return ResponseEntity.ok(kimchiPremiumService.getLatest());
    }
}
