package com.coinsensor.detectedcoin.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.detection.entity.Detection;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;

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
@Table(name = "detected_coins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DetectedCoin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "detected_coin_id")
	private Long detectedCoinId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "detection_id", nullable = false)
	private Detection detection;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "coin_id", nullable = false)
	private Coin coin;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exchange_coin_id", nullable = false)
	private ExchangeCoin exchangeCoin;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal volatility;

	@Column(nullable = false)
	private Double volume;

	@Column(nullable = false)
	private Double high;

	@Column(nullable = false)
	private Double low;

	@Column(name = "view_count", nullable = false)
	private Long viewCount;

	@Column(name = "detected_at", nullable = false)
	private LocalDateTime detectedAt;

	public DetectedCoin(Detection detection, Coin coin, ExchangeCoin exchangeCoin, BigDecimal volatility, Double volume,
		Double high, Double low, LocalDateTime detectedAt) {
		this.detection = detection;
		this.coin = coin;
		this.exchangeCoin = exchangeCoin;
		this.volatility = volatility;
		this.volume = volume;
		this.high = high;
		this.low = low;
		this.viewCount = 0L;
		this.detectedAt = detectedAt;
	}

	public void incrementViewCount() {
		this.viewCount++;
	}
}
