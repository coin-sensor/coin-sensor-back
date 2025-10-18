package com.coinsensor.uservote.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVoteRequest {
    private Long voteTopicId;
    private Long voteOptionId;
    private Long userId;
}
