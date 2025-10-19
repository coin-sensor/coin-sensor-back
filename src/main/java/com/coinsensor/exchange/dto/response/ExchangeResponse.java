package com.coinsensor.exchange.dto.response;

import com.coinsensor.exchange.entity.Exchange;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangeResponse {
    private Long exchangeId;
    private String name;
    
    public static ExchangeResponse from(Exchange entity) {
        return ExchangeResponse.builder()
                .exchangeId(entity.getExchangeId())
                .name(entity.getName())
                .build();
    }
}
