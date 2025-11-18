package com.coinsensor.detection.entity;

import java.time.LocalDateTime;

import com.coinsensor.conditions.entity.Condition;
import java.math.BigDecimal;
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

@Entity
@Table(name = "detections")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Detection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "detection_id")
	private Long detectionId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "condition_id", nullable = false)
	private Condition condition;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exchange_id", nullable = false)
	private Exchange exchange;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String summary;

	@Column(name = "detection_count", nullable = false)
	private Long detectionCount;

	@Column(name = "change_x_avg", nullable = false, precision = 10, scale = 2)
	private BigDecimal changeXAvg;

	@Column(name = "volume_x_avg", nullable = false, precision = 10, scale = 2)
	private BigDecimal volumeXAvg;

	@Column(name = "detected_at", nullable = false)
	private LocalDateTime detectedAt;

	public Detection(Condition condition, Exchange exchange, String summary, Long detectionCount, BigDecimal changeXAvg, BigDecimal volumeXAvg) {
		this.condition = condition;
		this.exchange = exchange;
		this.summary = summary;
		this.detectionCount = detectionCount;
		this.changeXAvg = changeXAvg;
		this.volumeXAvg = volumeXAvg;
		this.detectedAt = LocalDateTime.now();
	}

	public static Detection to(Condition condition, Exchange exchange, String summary,
		Long detectionCount, BigDecimal changeXAvg, BigDecimal volumeXAvg) {
		return Detection.builder()
			.condition(condition)
			.exchange(exchange)
			.summary(summary)
			.detectionCount(detectionCount)
			.changeXAvg(changeXAvg)
			.volumeXAvg(volumeXAvg)
			.detectedAt(LocalDateTime.now())
			.build();
	}
}
