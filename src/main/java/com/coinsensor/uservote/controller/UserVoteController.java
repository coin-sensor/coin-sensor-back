package com.coinsensor.uservote.controller;

import com.coinsensor.uservote.dto.request.UserVoteRequest;
import com.coinsensor.uservote.dto.response.UserVoteResponse;
import com.coinsensor.uservote.service.UserVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class UserVoteController {
    
    private final UserVoteService userVoteService;
    
    @PostMapping
    public ResponseEntity<UserVoteResponse> vote(@RequestBody UserVoteRequest request) {
        return ResponseEntity.ok(userVoteService.vote(request));
    }
}
