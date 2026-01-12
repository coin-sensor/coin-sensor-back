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

import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.userreaction.dto.request.UserReactionRequest;
import com.coinsensor.userreaction.dto.response.CoinReactionResponse;
import com.coinsensor.userreaction.dto.response.ReactionCountResponse;
import com.coinsensor.userreaction.dto.response.ReactionTrendDataResponse;
import com.coinsensor.userreaction.service.UserReactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class UserReactionController {

	private final UserReactionService userReactionService;

	@PostMapping
	public ResponseEntity<ApiResponse<List<ReactionCountResponse>>> toggleReaction(@RequestHeader String uuid,
		@RequestBody UserReactionRequest request) {
		List<ReactionCountResponse> updatedCounts = userReactionService.toggleReaction(uuid, request);
		return ApiResponse.createSuccess(updatedCounts);
	}

	@GetMapping("/topLiked")
	public ResponseEntity<ApiResponse<List<CoinReactionResponse>>> getTopLikedCoins(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "5") int limit) {
		return ApiResponse.createSuccess(userReactionService.getTopLikedCoins(days, limit));
	}

	@GetMapping("/topDisliked")
	public ResponseEntity<ApiResponse<List<CoinReactionResponse>>> getTopDislikedCoins(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "5") int limit) {
		return ApiResponse.createSuccess(userReactionService.getTopDislikedCoins(days, limit));
	}

	@GetMapping("/trend/like")
	public ResponseEntity<ApiResponse<List<ReactionTrendDataResponse>>> getLikeTrendData(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "5") int limit) {
		return ApiResponse.createSuccess(userReactionService.getLikeTrendData(days, limit));
	}

	@GetMapping("/trend/dislike")
	public ResponseEntity<ApiResponse<List<ReactionTrendDataResponse>>> getDislikeTrendData(
		@RequestParam(defaultValue = "1") int days,
		@RequestParam(defaultValue = "5") int limit) {
		return ApiResponse.createSuccess(userReactionService.getDislikeTrendData(days, limit));
	}

}