package com.coinsensor.favoritecoin.controller;

import com.coinsensor.favoritecoin.service.FavoriteCoinService;
import com.coinsensor.favoritecoin.dto.response.FavoriteCoinResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/favorite-coins")
@RequiredArgsConstructor
public class FavoriteCoinController {
    
    private final FavoriteCoinService favoriteCoinService;
    
    @GetMapping
    public ResponseEntity<List<FavoriteCoinResponse>> getFavoriteCoins(@RequestHeader String uuid) {
        return ResponseEntity.ok(favoriteCoinService.getFavoriteCoins(uuid));
    }
    
    @PostMapping("/{exchangeCoinId}")
    public ResponseEntity<Void> addFavoriteCoin(@RequestHeader String uuid, @PathVariable Long exchangeCoinId) {
        favoriteCoinService.addFavoriteCoin(uuid, exchangeCoinId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{exchangeCoinId}")
    public ResponseEntity<Void> removeFavoriteCoin(@RequestHeader String uuid, @PathVariable Long exchangeCoinId) {
        favoriteCoinService.removeFavoriteCoin(uuid, exchangeCoinId);
        return ResponseEntity.ok().build();
    }
}