package com.coinsensor.exchangecoin.entity;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.exchange.entity.Exchange;

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
import lombok.Setter;

@Entity
@Table(name = "exchange_coins")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExchangeCoin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "exchange_coin_id")
	private Long exchangeCoinId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exchange_id", nullable = false)
	private Exchange exchange;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "coin_id", nullable = false)
	private Coin coin;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Builder.Default
	@Column(name = "enable_detection", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
	private Boolean enableDetection = true;

}
