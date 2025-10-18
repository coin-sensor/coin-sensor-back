package com.coinsensor.voteoption.entity;

import com.coinsensor.votetopic.entity.VoteTopic;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vote_options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VoteOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_option_id")
    private Long voteOptionId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vote_topic_id", nullable = false)
    private VoteTopic voteTopic;
    
    @Column(nullable = false)
    private String name;
}
