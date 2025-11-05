package com.coinsensor.userreaction.dto.request;

import lombok.Getter;

@Getter
public class UserReactionRequest {
    private Long reactionId;
    private String targetTable;
    private Long targetId;
}