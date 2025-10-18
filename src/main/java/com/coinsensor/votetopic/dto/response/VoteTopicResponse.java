package com.coinsensor.votetopic.dto.response;

import com.coinsensor.votetopic.entity.VoteTopic;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteTopicResponse {
    private Long voteTopicId;
    private String title;
    private Boolean isActive;
    private LocalDateTime createdAt;
    
    public static VoteTopicResponse from(VoteTopic entity) {
        return VoteTopicResponse.builder()
                .voteTopicId(entity.getVoteTopicId())
                .title(entity.getTitle())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
