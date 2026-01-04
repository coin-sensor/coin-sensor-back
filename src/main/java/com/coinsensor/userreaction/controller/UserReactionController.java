package com.coinsensor.userreaction.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.userreaction.dto.request.UserReactionRequest;
import com.coinsensor.userreaction.dto.response.CoinReactionResponse;
import com.coinsensor.userreaction.dto.response.ReactionCountResponse;
import com.coinsensor.userreaction.service.UserReactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class UserReactionController {

	private final UserReactionService userReactionService;

	@PostMapping
	public ResponseEntity<List<ReactionCountResponse>> toggleReaction(@RequestHeader String uuid, @RequestBody UserReactionRequest request) {
		List<ReactionCountResponse> updatedCounts = userReactionService.toggleReaction(uuid, request);
		return ResponseEntity.ok(updatedCounts);
	}

	@GetMapping("/counts")
	public ResponseEntity<List<ReactionCountResponse>> getReactionCounts(
		@RequestParam String targetType,
		@RequestParam Long targetId) {
		List<ReactionCountResponse> counts = userReactionService.getReactionCounts(targetType, targetId);
		return ResponseEntity.ok(counts);
	}

	@GetMapping("/topLiked")
	public ResponseEntity<List<CoinReactionResponse>> getTopLikedCoins(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "10") int limit) {
		return ResponseEntity.ok(userReactionService.getTopLikedCoins(days, limit));
	}

	@GetMapping("/topDisliked")
	public ResponseEntity<List<CoinReactionResponse>> getTopDislikedCoins(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "10") int limit) {
		return ResponseEntity.ok(userReactionService.getTopDislikedCoins(days, limit));
	}

	// @GetMapping("/trend")
	// public ResponseEntity<List<ReactionTrendDataResponse>> getReactionsTrendData(
	// 	@RequestParam(defaultValue = "1") int days,
	// 	@RequestParam(defaultValue = "10") int limit) {
	// 	return ResponseEntity.ok(userReactionService.getReactionsTrendData(days, limit));
	// }

}