package com.coinsensor.userreaction.entity;

import com.coinsensor.user.entity.User;
import com.coinsensor.reaction.entity.Reaction;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_reactions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserReaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_reaction_id")
    private Long userReactionId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reaction_id")
    private Reaction reaction;
    
    @Column(name = "target_table", nullable = false)
    private String targetTable;
    
    @Column(name = "target_id", nullable = false)
    private Long targetId;
}