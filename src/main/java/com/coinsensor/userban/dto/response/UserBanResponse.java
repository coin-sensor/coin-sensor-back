package com.coinsensor.userban.dto.response;

import com.coinsensor.userban.entity.UserBan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserBanResponse {
    private Long userBanId;
    private Long userId;
    private Long banTypeId;
    private String reason;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    public static UserBanResponse from(UserBan userBan) {
        return UserBanResponse.builder()
                .userBanId(userBan.getUserBanId())
                .userId(userBan.getUser().getUserId())
                .banTypeId(userBan.getBanType().getBanTypeId())
                .reason(userBan.getBanType().getReason())
                .startTime(userBan.getStartTime())
                .endTime(userBan.getEndTime())
                .build();
    }
}