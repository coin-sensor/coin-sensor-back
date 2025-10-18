package com.coinsensor.uservote.entity;

import com.coinsensor.user.entity.User;
import com.coinsensor.voteoption.entity.VoteOption;
import com.coinsensor.votetopic.entity.VoteTopic;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_votes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserVote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vote_topic_id", nullable = false)
    private VoteTopic voteTopic;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vote_option_id", nullable = false)
    private VoteOption voteOption;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "voted_at", nullable = false)
    private LocalDateTime votedAt;
}
