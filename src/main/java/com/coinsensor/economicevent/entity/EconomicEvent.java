package com.coinsensor.economicevent.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "economic_events")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class EconomicEvent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long eventId;
    
    @Column(name = "event_name", nullable = false)
    private String eventName;
    
    @Column
    private String country;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('low', 'medium', 'high')")
    private Importance importance;
    
    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "related_assets")
    private String relatedAssets;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public enum Importance {
        low, medium, high
    }
}
