package com.coinsensor.websocket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.websocket.service.WebSocketSessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/websocket")
@RequiredArgsConstructor
public class WebSocketController {

	private final WebSocketSessionService webSocketSessionService;

	@GetMapping("/activeUsers")
	public ResponseEntity<Integer> getActiveUserCount() {
		return ResponseEntity.ok(webSocketSessionService.getActiveUserCount());
	}
}