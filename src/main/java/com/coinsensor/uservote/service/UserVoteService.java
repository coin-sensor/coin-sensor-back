package com.coinsensor.uservote.service;

import com.coinsensor.uservote.dto.request.UserVoteRequest;
import com.coinsensor.uservote.dto.response.UserVoteResponse;

public interface UserVoteService {
    UserVoteResponse vote(UserVoteRequest request);
}
