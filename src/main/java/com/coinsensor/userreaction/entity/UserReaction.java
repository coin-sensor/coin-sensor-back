package com.coinsensor.userreaction.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.coinsensor.reaction.entity.Reaction;
import com.coinsensor.targettable.entity.TargetTable;
import com.coinsensor.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "reaction_id")
	private Reaction reaction;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "target_table_id")
	private TargetTable targetTable;

	@Column(name = "target_id", nullable = false)
	private Long targetId;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public static UserReaction to(User user, Reaction reaction, TargetTable targetTable,
		Long targetId) {
		return UserReaction.builder()
			.user(user)
			.reaction(reaction)
			.targetTable(targetTable)
			.targetId(targetId)
			.build();
	}

	public void updateReaction(Reaction reaction) {
		this.reaction = reaction;
	}
}