package com.coinsensor.userignore.entity;

import java.time.LocalDateTime;

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
@Table(name = "user_ignores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserIgnore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_ignore_id")
	private Long userIgnoreId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_target_id")
	private User targetUser;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	public static UserIgnore to(User user, User targetUser) {
		return UserIgnore.builder()
			.user(user)
			.targetUser(targetUser)
			.createdAt(LocalDateTime.now())
			.build();
	}
}