package com.coinsensor.uservote.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.uservote.dto.request.UserVoteRequest;
import com.coinsensor.uservote.dto.response.UserVoteResponse;
import com.coinsensor.uservote.service.UserVoteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class UserVoteController {

	private final UserVoteService userVoteService;

	@PostMapping
	public ResponseEntity<ApiResponse<UserVoteResponse>> vote(@RequestBody UserVoteRequest request) {
		return ApiResponse.createSuccess(userVoteService.vote(request));
	}
}
