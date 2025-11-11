package com.coinsensor.clickcoin.entity;

import java.time.LocalDateTime;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
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
@Table(name = "click_coins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ClickCoin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "click_coin_id")
	private Long clickCoinId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "detected_coin_id")
	private DetectedCoin detectedCoin;

	@Column(nullable = false)
	private Long count;

	@Column(name = "clicked_at", nullable = false)
	private LocalDateTime clickedAt;

	public ClickCoin(User user, DetectedCoin detectedCoin) {
		this.user = user;
		this.detectedCoin = detectedCoin;
		this.count = 1L;
		this.clickedAt = LocalDateTime.now();
	}

	public void reClick() {
		this.count++;
		this.clickedAt = LocalDateTime.now();
	}
}