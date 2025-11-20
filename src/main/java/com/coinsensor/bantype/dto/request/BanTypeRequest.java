package com.coinsensor.bantype.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BanTypeRequest {
    private String reason;
    private Long period;
}