package com.coinsensor.votetopic.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.votetopic.dto.response.VoteTopicResponse;
import com.coinsensor.votetopic.service.VoteTopicService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteTopicController {

	private final VoteTopicService voteTopicService;

	@GetMapping("/topics")
	public ResponseEntity<ApiResponse<List<VoteTopicResponse>>> getActiveTopics() {
		return ApiResponse.createSuccess(voteTopicService.getActiveTopics());
	}
}
