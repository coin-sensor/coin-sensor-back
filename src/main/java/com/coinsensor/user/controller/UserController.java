package com.coinsensor.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.dto.ApiResponse;
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
	public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@RequestHeader("uuid") String uuid) {
		return ApiResponse.createSuccess(userService.getUserInfo(uuid));
	}

	@PutMapping("/nickname")
	public ResponseEntity<ApiResponse<UserInfoResponse>> updateNickname(
		@RequestHeader("uuid") String uuid,
		@RequestBody UpdateNicknameRequest request) {
		return ApiResponse.createSuccess(userService.updateNickname(uuid, request.getNickname()));
	}

	@GetMapping("/role")
	public ResponseEntity<ApiResponse<String>> getUserRole(@RequestHeader("uuid") String uuid) {
		return ApiResponse.createSuccess(userService.getUserRole(uuid));
	}

	@GetMapping("/admin")
	public ResponseEntity<ApiResponse<Boolean>> isAdmin(@RequestHeader("uuid") String uuid) {
		return ApiResponse.createSuccess(userService.isAdmin(uuid));
	}
}