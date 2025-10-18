package com.coinsensor.economicevent.dto.response;

import com.coinsensor.economicevent.entity.EconomicEvent;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EconomicEventResponse {
    private Long eventId;
    private String eventName;
    private String country;
    private String importance;
    private LocalDateTime eventTime;
    private String description;
    private String relatedAssets;
    
    public static EconomicEventResponse from(EconomicEvent entity) {
        return EconomicEventResponse.builder()
                .eventId(entity.getEventId())
                .eventName(entity.getEventName())
                .country(entity.getCountry())
                .importance(entity.getImportance() != null ? entity.getImportance().name() : null)
                .eventTime(entity.getEventTime())
                .description(entity.getDescription())
                .relatedAssets(entity.getRelatedAssets())
                .build();
    }
}
