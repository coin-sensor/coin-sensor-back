package com.coinsensor.uservote.dto.response;

import com.coinsensor.uservote.entity.UserVote;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVoteResponse {
    private Long voteId;
    private Long voteTopicId;
    private Long voteOptionId;
    private Long userId;
    private LocalDateTime votedAt;
    
    public static UserVoteResponse from(UserVote entity) {
        return UserVoteResponse.builder()
                .voteId(entity.getVoteId())
                .voteTopicId(entity.getVoteTopic().getVoteTopicId())
                .voteOptionId(entity.getVoteOption().getVoteOptionId())
                .userId(entity.getUser().getUserId())
                .votedAt(entity.getVotedAt())
                .build();
    }
}
