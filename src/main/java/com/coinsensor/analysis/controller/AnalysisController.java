package com.coinsensor.analysis.controller;

import com.coinsensor.analysis.dto.response.AnalysisResponse;
import com.coinsensor.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {
    
    private final AnalysisService btcAiReportService;
    
    @GetMapping("/daily-report")
    public ResponseEntity<AnalysisResponse> getDailyReport() {
        return ResponseEntity.ok(btcAiReportService.getLatestReport());
    }
}
