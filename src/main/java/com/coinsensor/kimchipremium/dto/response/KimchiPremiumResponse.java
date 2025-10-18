package com.coinsensor.kimchipremium.dto.response;

import com.coinsensor.kimchipremium.entity.KimchiPremium;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KimchiPremiumResponse {
    private Double binanceBtcUsdt;
    private Double upbitBtcKrw;
    private Double kimchiPremium;
    private LocalDateTime createdAt;
    
    public static KimchiPremiumResponse from(KimchiPremium entity) {
        return KimchiPremiumResponse.builder()
                .binanceBtcUsdt(entity.getBinanceBtcUsdt())
                .upbitBtcKrw(entity.getUpbitBtcKrw())
                .kimchiPremium(entity.getKimchiPremium())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
