package com.coinsensor.channel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.channel.dto.request.ChannelRequest;
import com.coinsensor.channel.dto.response.ChannelResponse;
import com.coinsensor.channel.service.ChannelService;
import com.coinsensor.common.annotation.AuthorizeRole;
import com.coinsensor.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

	private final ChannelService channelService;

	@AuthorizeRole
	@PostMapping
	public ResponseEntity<ApiResponse<ChannelResponse>> createChannel(@RequestBody ChannelRequest request) {
		return ApiResponse.createSuccess(channelService.createChannel(request));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<ChannelResponse>>> getAllChannels() {
		return ApiResponse.createSuccess(channelService.getAllChannels());
	}

	@AuthorizeRole
	@GetMapping("/{channelId}")
	public ResponseEntity<ApiResponse<ChannelResponse>> getChannelById(@PathVariable Long channelId) {
		return ApiResponse.createSuccess(channelService.getChannelById(channelId));
	}

	@AuthorizeRole
	@PutMapping("/{channelId}")
	public ResponseEntity<ApiResponse<ChannelResponse>> updateChannel(@PathVariable Long channelId,
		@RequestBody ChannelRequest request) {
		return ApiResponse.createSuccess(channelService.updateChannel(channelId, request));
	}

	@AuthorizeRole
	@DeleteMapping("/{channelId}")
	public ResponseEntity<ApiResponse<Void>> deleteChannel(@PathVariable Long channelId) {
		channelService.deleteChannel(channelId);
		return ResponseEntity.ok().build();
	}
}