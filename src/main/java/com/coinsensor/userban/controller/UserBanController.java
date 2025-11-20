package com.coinsensor.userban.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.userban.dto.request.UserBanRequest;
import com.coinsensor.userban.dto.response.UserBanResponse;
import com.coinsensor.userban.service.UserBanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/userBans")
@RequiredArgsConstructor
public class UserBanController {

	private final UserBanService userBanService;

	@PostMapping
	public ResponseEntity<UserBanResponse> banUser(@RequestBody UserBanRequest request) {
		UserBanResponse response = userBanService.banUser(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<UserBanResponse>> getAllBannedUsers() {
		List<UserBanResponse> responses = userBanService.getAllBannedUsers();
		return ResponseEntity.ok(responses);
	}

	@DeleteMapping("/{userBanId}")
	public ResponseEntity<Void> unbanUser(@PathVariable Long userBanId) {
		userBanService.unbanUser(userBanId);
		return ResponseEntity.ok().build();
	}
}