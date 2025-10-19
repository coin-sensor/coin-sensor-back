package com.coinsensor.detectiongroup.entity;

import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "detection_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DetectionGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detection_group_id")
    private Long detectionGroupId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detection_criteria_id", nullable = false)
    private DetectionCriteria detectionCriteria;
    
    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt;
    
    @Column(name = "detection_count", nullable = false)
    private Long detectionCount;
    
    @Column(columnDefinition = "TEXT", nullable = false)
    private String summary;
    
    public DetectionGroup(DetectionCriteria detectionCriteria, LocalDateTime detectedAt, Long detectionCount, String summary) {
        this.detectionCriteria = detectionCriteria;
        this.detectedAt = detectedAt;
        this.detectionCount = detectionCount;
        this.summary = summary;
    }
}
