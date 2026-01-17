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
	@JoinColumn(name = "exchange_coin_id", nullable = false)
	private ExchangeCoin exchangeCoin;

	@Column(name = "change_x", nullable = false, precision = 10, scale = 2)
	private BigDecimal changeX;

	@Column(name = "volume_x", nullable = false, precision = 10, scale = 2)
	private BigDecimal volumeX;

	@Column(nullable = false, precision = 20, scale = 8)
	private BigDecimal high;

	@Column(nullable = false, precision = 20, scale = 8)
	private BigDecimal low;

	@Column(name = "view_count", nullable = false)
	private Long viewCount;

	@Column(name = "like_count", nullable = false)
	private Long likeCount;

	@Column(name = "dislike_count", nullable = false)
	private Long dislikeCount;

	@Column(name = "detected_at", nullable = false)
	private LocalDateTime detectedAt;

	public DetectedCoin(Detection detection, Coin coin, ExchangeCoin exchangeCoin, BigDecimal changeX,
		BigDecimal volumeX,
		BigDecimal high, BigDecimal low, LocalDateTime detectedAt) {
		this.detection = detection;
		this.exchangeCoin = exchangeCoin;
		this.changeX = changeX;
		this.volumeX = volumeX;
		this.high = high;
		this.low = low;
		this.viewCount = 0L;
		this.likeCount = 0L;
		this.dislikeCount = 0L;
		this.detectedAt = detectedAt;
	}

	public static DetectedCoin to(ExchangeCoin exchangeCoin, BigDecimal changeX, BigDecimal volumeX,
		BigDecimal high, BigDecimal low) {
		return DetectedCoin.builder()
			.exchangeCoin(exchangeCoin)
			.changeX(changeX)
			.volumeX(volumeX)
			.high(high)
			.low(low)
			.viewCount(0L)
			.likeCount(0L)
			.dislikeCount(0L)
			.detectedAt(LocalDateTime.now())
			.build();
	}

	public DetectedCoin withDetection(Detection detection) {
		return DetectedCoin.builder()
			.detection(detection)
			.exchangeCoin(this.exchangeCoin)
			.changeX(this.changeX)
			.volumeX(this.volumeX)
			.high(this.high)
			.low(this.low)
			.viewCount(0L)
			.likeCount(0L)
			.dislikeCount(0L)
			.detectedAt(this.detectedAt)
			.build();
	}

	public void incrementViewCount() {
		this.viewCount++;
	}

	public void incrementLikeCount() {
		this.likeCount++;
	}

	public void decrementLikeCount() {
		if (this.likeCount > 0) {
			this.likeCount--;
		}
	}

	public void incrementDislikeCount() {
		this.dislikeCount++;
	}

	public void decrementDislikeCount() {
		if (this.dislikeCount > 0) {
			this.dislikeCount--;
		}
	}
}
