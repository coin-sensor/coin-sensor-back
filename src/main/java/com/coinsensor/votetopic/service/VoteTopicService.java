package com.coinsensor.votetopic.service;

import com.coinsensor.votetopic.dto.response.VoteTopicResponse;
import java.util.List;

public interface VoteTopicService {
    List<VoteTopicResponse> getActiveTopics();
}
