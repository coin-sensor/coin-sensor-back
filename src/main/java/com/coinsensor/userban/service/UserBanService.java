package com.coinsensor.userban.service;

import com.coinsensor.userban.dto.request.UserBanRequest;
import com.coinsensor.userban.dto.response.UserBanResponse;
import java.util.List;

public interface UserBanService {

    UserBanResponse banUser(UserBanRequest request);

    List<UserBanResponse> getAllBannedUsers();

    void unbanUser(Long userBanId);
}