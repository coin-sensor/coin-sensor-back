package com.coinsensor.votetopic.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vote_topics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VoteTopic {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_topic_id")
    private Long voteTopicId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public VoteTopic(String title, Boolean isActive, LocalDateTime createdAt) {
        this.title = title;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
}
