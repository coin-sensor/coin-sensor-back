package com.coinsensor.ohlcvs.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.timeframe.entity.Timeframe;
import com.coinsensor.websocket.dto.KlineData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ohlcvs", indexes = {
	@Index(name = "idx_ohlcv_exchange_timeframe_starttime",
		columnList = "exchange_coin_id, timeframe_id, start_time DESC")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ohlcv {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ohlcv_id")
	private Long ohlcvId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exchange_coin_id", nullable = false)
	private ExchangeCoin exchangeCoin;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "timeframe_id", nullable = false)
	private Timeframe timeframe;

	@Column(nullable = false, precision = 20, scale = 8)
	private BigDecimal open;

	@Column(nullable = false, precision = 20, scale = 8)
	private BigDecimal high;

	@Column(nullable = false, precision = 20, scale = 8)
	private BigDecimal low;

	@Column(nullable = false, precision = 20, scale = 8)
	private BigDecimal close;

	@Column(nullable = false, precision = 30, scale = 8)
	private BigDecimal volume;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "start_time")
	private LocalDateTime startTime;

	@Column(name = "close_time")
	private LocalDateTime closeTime;

	public Ohlcv(ExchangeCoin exchangeCoin, Timeframe timeframe, BigDecimal open, BigDecimal high, BigDecimal low,
		BigDecimal close, BigDecimal volume, LocalDateTime createdAt) {
		this.exchangeCoin = exchangeCoin;
		this.timeframe = timeframe;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.volume = volume;
		this.createdAt = createdAt;
	}

	public static Ohlcv to(ExchangeCoin exchangeCoin, Timeframe timeframe, BigDecimal open, BigDecimal high,
		BigDecimal low, BigDecimal close, BigDecimal volume, LocalDateTime createdAt) {
		return new Ohlcv(exchangeCoin, timeframe, open, high, low, close, volume, createdAt);
	}

	public static Ohlcv from(KlineData.KlineInfo kline, ExchangeCoin exchangeCoin, Timeframe timeframe) {
		ZoneId kstZone = java.time.ZoneId.of("Asia/Seoul");
		return Ohlcv.builder()
			.exchangeCoin(exchangeCoin)
			.timeframe(timeframe)
			.open(kline.getOpenPrice())
			.high(kline.getHighPrice())
			.low(kline.getLowPrice())
			.close(kline.getClosePrice())
			.volume(kline.getVolumeAmount())
			.startTime(LocalDateTime.ofEpochSecond(kline.getStartTime() / 1000, 0, java.time.ZoneOffset.UTC)
				.atZone(java.time.ZoneOffset.UTC)
				.withZoneSameInstant(kstZone)
				.toLocalDateTime())
			.closeTime(LocalDateTime.ofEpochSecond(kline.getCloseTime() / 1000, 0, java.time.ZoneOffset.UTC)
				.atZone(java.time.ZoneOffset.UTC)
				.withZoneSameInstant(kstZone)
				.toLocalDateTime())
			.createdAt(LocalDateTime.now())
			.build();
	}
}