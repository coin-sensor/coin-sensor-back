package com.coinsensor.bantype.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ban_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BanType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ban_type_id")
    private Long banTypeId;
    
    @Column(nullable = false)
    private String reason;
    
    @Column(nullable = false)
    private Long period;
}