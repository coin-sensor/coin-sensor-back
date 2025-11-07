package com.coinsensor.reaction.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reaction_id")
    private Long reactionId;
    
    @Column(nullable = false)
    private String name;

    public Reaction(String name) {
        this.name = name;
    }
}