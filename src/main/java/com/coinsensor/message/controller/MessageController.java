package com.coinsensor.message.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.message.dto.response.MessageResponse;
import com.coinsensor.message.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/channels/{channelId}/messages")
@RequiredArgsConstructor
public class MessageController {

	private final MessageService chatMessageService;

	@GetMapping
	public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessages(
		@PathVariable Long channelId,
		@RequestParam(defaultValue = "20") int limit) {
		return ApiResponse.createSuccess(chatMessageService.getRecentMessages(channelId, limit));
	}

	@GetMapping("/before/{lastMessageId}")
	public ResponseEntity<ApiResponse<List<MessageResponse>>> getMessagesBefore(
		@PathVariable Long channelId,
		@PathVariable Long lastMessageId,
		@RequestParam(defaultValue = "20") int limit) {
		return ApiResponse.createSuccess(chatMessageService.getMessagesBefore(channelId, lastMessageId, limit));
	}

	@PostMapping
	public ResponseEntity<ApiResponse<MessageResponse>> saveMessage(@PathVariable String channelId,
		@RequestBody MessageRequest request) {
		return ApiResponse.createSuccess(chatMessageService.saveMessage(request));
	}

	@DeleteMapping("/{messageId}")
	public ResponseEntity<ApiResponse<Void>> deleteMessage(@PathVariable String channelId,
		@PathVariable Long messageId) {
		chatMessageService.deleteMessage(messageId);
		return ResponseEntity.ok().build();
	}
}
