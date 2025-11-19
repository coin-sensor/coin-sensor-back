package com.coinsensor.favoritecoin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.favoritecoin.dto.response.FavoriteCoinResponse;
import com.coinsensor.favoritecoin.service.FavoriteCoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favoriteCoins")
@RequiredArgsConstructor
public class FavoriteCoinController {

	private final FavoriteCoinService favoriteCoinService;

	@GetMapping
	public ResponseEntity<List<FavoriteCoinResponse>> getFavoriteCoins(@RequestHeader String uuid) {
		return ResponseEntity.ok(favoriteCoinService.getFavoriteCoins(uuid));
	}

	@PostMapping("/{exchangeCoinId}")
	public ResponseEntity<Void> createOrDeleteFavoriteCoin(@RequestHeader String uuid,
		@PathVariable Long exchangeCoinId) {
		favoriteCoinService.createOrDeleteFavoriteCoin(uuid, exchangeCoinId);
		return ResponseEntity.ok().build();
	}
}