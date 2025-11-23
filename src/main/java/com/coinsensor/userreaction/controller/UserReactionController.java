package com.coinsensor.userreaction.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.userreaction.dto.request.UserReactionRequest;
import com.coinsensor.userreaction.service.UserReactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
public class UserReactionController {

	private final UserReactionService userReactionService;

	@PostMapping
	public ResponseEntity<Void> toggleReaction(@RequestHeader String uuid, @RequestBody UserReactionRequest request) {
		userReactionService.toggleReaction(uuid, request);
		return ResponseEntity.ok().build();
	}
}