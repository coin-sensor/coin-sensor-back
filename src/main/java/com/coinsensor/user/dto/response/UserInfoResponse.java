package com.coinsensor.user.dto.response;

import com.coinsensor.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {
    private String uuid;
    private String ipAddress;
    private String nickname;
    
    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .uuid(user.getUuid())
                .ipAddress(user.getIpAddress())
                .nickname(user.getNickname())
                .build();
    }
}