package com.coinsensor.detectiongroup.controller;

import com.coinsensor.detectedcoin.dto.response.DetectedCoinGroupResponse;
import com.coinsensor.detectiongroup.service.DetectionGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/detection")
@RequiredArgsConstructor
public class DetectionGroupController {
    
    private final DetectionGroupService detectionGroupService;
    
    @GetMapping
    public ResponseEntity<List<DetectedCoinGroupResponse>> getDetectionGroups(
            @RequestParam String exchange,
            @RequestParam String exchangeType,
            @RequestParam String timeframe) {
        return ResponseEntity.ok(detectionGroupService.getDetectionGroups(exchange, exchangeType, timeframe));
    }
}