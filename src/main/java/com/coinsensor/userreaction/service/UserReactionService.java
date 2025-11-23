package com.coinsensor.userreaction.service;

import com.coinsensor.userreaction.dto.request.UserReactionRequest;

public interface UserReactionService {

    void toggleReaction(String userUuid, UserReactionRequest request);
}