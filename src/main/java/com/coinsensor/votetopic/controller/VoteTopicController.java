package com.coinsensor.votetopic.controller;

import com.coinsensor.votetopic.dto.response.VoteTopicResponse;
import com.coinsensor.votetopic.service.VoteTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vote")
@RequiredArgsConstructor
public class VoteTopicController {
    
    private final VoteTopicService voteTopicService;
    
    @GetMapping("/topics")
    public ResponseEntity<List<VoteTopicResponse>> getActiveTopics() {
        return ResponseEntity.ok(voteTopicService.getActiveTopics());
    }
}
