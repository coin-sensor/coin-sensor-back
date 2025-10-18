package com.coinsensor.btcaireport.controller;

import com.coinsensor.btcaireport.dto.response.BtcAiReportResponse;
import com.coinsensor.btcaireport.service.BtcAiReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BtcAiReportController {
    
    private final BtcAiReportService btcAiReportService;
    
    @GetMapping("/daily-report")
    public ResponseEntity<BtcAiReportResponse> getDailyReport() {
        return ResponseEntity.ok(btcAiReportService.getLatestReport());
    }
}
