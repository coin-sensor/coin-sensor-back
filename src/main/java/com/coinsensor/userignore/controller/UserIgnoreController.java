package com.coinsensor.userignore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.userignore.dto.request.UserIgnoreRequest;
import com.coinsensor.userignore.service.UserIgnoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/userIgnores")
@RequiredArgsConstructor
public class UserIgnoreController {

	private final UserIgnoreService userIgnoreService;

	@PostMapping
	public ResponseEntity<ApiResponse<Void>> ignoreUser(@RequestHeader("uuid") String uuid,
		@RequestBody UserIgnoreRequest request) {
		userIgnoreService.ignoreUser(uuid, request.getUserId());
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> unignoreUser(@RequestHeader("uuid") String uuid,
		@RequestBody UserIgnoreRequest request) {
		userIgnoreService.unignoreUser(uuid, request.getUserId());
		return ResponseEntity.ok().build();
	}
}