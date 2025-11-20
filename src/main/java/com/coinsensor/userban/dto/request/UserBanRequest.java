package com.coinsensor.userban.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserBanRequest {
    private Long userId;
    private Long banTypeId;
}