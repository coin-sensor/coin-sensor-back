package com.coinsensor.userreaction.dto.request;

import lombok.Getter;

@Getter
public class UserReactionRequest {
    private String reactionName;
    private String tableName;
    private Long targetId;
}