package com.coinsensor.bantype.dto.response;

import com.coinsensor.bantype.entity.BanType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BanTypeResponse {
    private Long banTypeId;
    private String reason;
    private Long period;
    
    public static BanTypeResponse from(BanType banType) {
        return BanTypeResponse.builder()
                .banTypeId(banType.getBanTypeId())
                .reason(banType.getReason())
                .period(banType.getPeriod())
                .build();
    }
}