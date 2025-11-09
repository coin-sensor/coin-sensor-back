package com.coinsensor.detection.controller;

import com.coinsensor.detection.dto.response.DetectionInfoResponse;
import com.coinsensor.detection.service.DetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/detection")
@RequiredArgsConstructor
public class DetectionController {
    
    private final DetectionService detectionService;
    
    @GetMapping
    public ResponseEntity<List<DetectionInfoResponse>> getDetections(
            @RequestParam String exchange,
            @RequestParam String exchangeType,
            @RequestParam String timeframe) {
        return ResponseEntity.ok(detectionService.getDetections(exchange, exchangeType, timeframe));
    }
}