package com.coinsensor.bantype.controller;

import com.coinsensor.bantype.dto.request.BanTypeRequest;
import com.coinsensor.bantype.dto.response.BanTypeResponse;
import com.coinsensor.bantype.service.BanTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banTypes")
@RequiredArgsConstructor
public class BanTypeController {
    
    private final BanTypeService banTypeService;
    
    @PostMapping
    public ResponseEntity<BanTypeResponse> createBanType(@RequestBody BanTypeRequest request) {
        BanTypeResponse response = banTypeService.createBanType(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<BanTypeResponse>> getAllBanTypes() {
        List<BanTypeResponse> responses = banTypeService.getAllBanTypes();
        return ResponseEntity.ok(responses);
    }
    
    @PutMapping("/{banTypeId}")
    public ResponseEntity<BanTypeResponse> updateBanType(@PathVariable Long banTypeId, @RequestBody BanTypeRequest request) {
        BanTypeResponse response = banTypeService.updateBanType(banTypeId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{banTypeId}")
    public ResponseEntity<Void> deleteBanType(@PathVariable Long banTypeId) {
        banTypeService.deleteBanType(banTypeId);
        return ResponseEntity.ok().build();
    }
}