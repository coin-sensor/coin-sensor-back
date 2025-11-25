package com.coinsensor.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.user.dto.request.UpdateNicknameRequest;
import com.coinsensor.user.dto.response.UserInfoResponse;
import com.coinsensor.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/info")
	public ResponseEntity<UserInfoResponse> getUserInfo(@RequestHeader("uuid") String uuid) {
		return ResponseEntity.ok(userService.getUserInfo(uuid));
	}

	@PutMapping("/nickname")
	public ResponseEntity<UserInfoResponse> updateNickname(
		@RequestHeader("uuid") String uuid,
		@RequestBody UpdateNicknameRequest request) {
		return ResponseEntity.ok(userService.updateNickname(uuid, request.getNickname()));
	}

	@GetMapping("/role")
	public ResponseEntity<String> getUserRole(@RequestHeader("uuid") String uuid) {
		return ResponseEntity.ok(userService.getUserRole(uuid));
	}

	@GetMapping("/admin")
	public ResponseEntity<Boolean> isAdmin(@RequestHeader("uuid") String uuid) {
		return ResponseEntity.ok(userService.isAdmin(uuid));
	}
}