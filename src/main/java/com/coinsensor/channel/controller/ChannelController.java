package com.coinsensor.channel.controller;

import com.coinsensor.channel.dto.request.ChannelRequest;
import com.coinsensor.channel.dto.response.ChannelResponse;
import com.coinsensor.channel.service.ChannelService;
import com.coinsensor.common.annotation.AuthorizeRole;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {
    
    private final ChannelService channelService;

    @AuthorizeRole
    @PostMapping
    public ResponseEntity<ChannelResponse> createChannel(@RequestBody ChannelRequest request) {
        return ResponseEntity.ok(channelService.createChannel(request));
    }

    @GetMapping
    public ResponseEntity<List<ChannelResponse>> getAllChannels() {
        return ResponseEntity.ok(channelService.getAllChannels());
    }

    @AuthorizeRole
    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> getChannelById(@PathVariable Long channelId) {
        return ResponseEntity.ok(channelService.getChannelById(channelId));
    }

    @AuthorizeRole
    @PutMapping("/{channelId}")
    public ResponseEntity<ChannelResponse> updateChannel(@PathVariable Long channelId, @RequestBody ChannelRequest request) {
        return ResponseEntity.ok(channelService.updateChannel(channelId, request));
    }

    @AuthorizeRole
    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable Long channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.ok().build();
    }
}