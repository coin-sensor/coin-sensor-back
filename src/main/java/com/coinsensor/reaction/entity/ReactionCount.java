package com.coinsensor.reaction.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "reaction_counts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReactionCount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "reaction_count_id")
	private Long reactionCountId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reaction_id")
	private Reaction reaction;

	@Column(name = "target_id", nullable = false)
	private Long targetId;

	@Column(name = "target_type", nullable = false)
	private String targetType;

	@Column(nullable = false)
	private Long count;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public static ReactionCount to(Long targetId, String targetType, Reaction reaction, Long count) {
		return ReactionCount.builder()
			.targetId(targetId)
			.targetType(targetType)
			.reaction(reaction)
			.count(count)
			.build();
	}

	public void incrementCount() {
		this.count++;
	}

	public void decrementCount() {
		this.count--;
	}
}