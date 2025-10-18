package com.coinsensor.feargreedindex.controller;

import com.coinsensor.feargreedindex.dto.response.FearGreedIndexResponse;
import com.coinsensor.feargreedindex.service.FearGreedIndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FearGreedIndexController {
    
    private final FearGreedIndexService fearGreedIndexService;
    
    @GetMapping("/fear-greed")
    public ResponseEntity<FearGreedIndexResponse> getLatestFearGreedIndex() {
        return ResponseEntity.ok(fearGreedIndexService.getLatest());
    }
}
