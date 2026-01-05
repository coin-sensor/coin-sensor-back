package com.coinsensor.ohlcvs.controller;

import com.coinsensor.common.annotation.AuthorizeRole;
import com.coinsensor.ohlcvs.service.OhlcvService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ohlcvs")
@RequiredArgsConstructor
public class OhlcvController {
    
    private final OhlcvService ohlcvService;
    
    @AuthorizeRole
    @DeleteMapping("/cleanup")
    public ResponseEntity<String> cleanupOldData(@RequestParam(defaultValue = "1") int years) {
        long deletedCount = ohlcvService.cleanupOldData(years);
        return ResponseEntity.ok(String.format("%d년 이상 된 OHLCV 데이터 %d건이 삭제되었습니다.", years, deletedCount));
    }
}