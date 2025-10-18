package com.coinsensor.votetopic.service;

import com.coinsensor.votetopic.dto.response.VoteTopicResponse;
import com.coinsensor.votetopic.repository.VoteTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteTopicServiceImpl implements VoteTopicService {
    
    private final VoteTopicRepository voteTopicRepository;
    
    @Override
    public List<VoteTopicResponse> getActiveTopics() {
        return voteTopicRepository.findAll().stream()
                .filter(topic -> topic.getIsActive())
                .map(VoteTopicResponse::from)
                .toList();
    }
}
