package com.coinsensor.user.service;

import com.coinsensor.user.dto.response.UserInfoResponse;
import com.coinsensor.user.entity.User;

public interface UserService {
    User getUserByUuid(String uuid);
    UserInfoResponse getUserInfo(String uuid);
    UserInfoResponse updateNickname(String uuid, String nickname);
    String getUserRole(String uuid);
    boolean isAdmin(String uuid);
}