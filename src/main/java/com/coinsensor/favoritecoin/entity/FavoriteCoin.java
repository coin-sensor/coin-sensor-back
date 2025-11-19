package com.coinsensor.favoritecoin.entity;

import java.time.LocalDateTime;

import com.coinsensor.exchangecoin.entity.ExchangeCoin;
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
@Table(name = "favorite_coins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FavoriteCoin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "favorite_coin_id")
	private Long favoriteCoinId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exchange_coin_id")
	private ExchangeCoin exchangeCoin;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	public static FavoriteCoin to(User user, ExchangeCoin exchangeCoin) {
		return FavoriteCoin.builder()
			.user(user)
			.exchangeCoin(exchangeCoin)
			.createdAt(LocalDateTime.now())
			.build();
	}
}