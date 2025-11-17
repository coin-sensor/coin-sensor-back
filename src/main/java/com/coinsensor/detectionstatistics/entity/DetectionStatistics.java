package com.coinsensor.detectionstatistics.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.coinsensor.timeframe.entity.Timeframe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detection_statistics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class DetectionStatistics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "detection_statistic_id")
	private Long detectionStatisticId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "timeframe_id", nullable = false)
	private Timeframe timeframe;

	@Column(name = "count", nullable = false)
	private Long count;

	@Column(name = "volatility_avg", nullable = false)
	private Double volatilityAvg;

	@Column(name = "volume_ratio_avg", nullable = false)
	private Double volumeRatioAvg;

	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Builder
	public DetectionStatistics(Timeframe timeframe, Long count, Double volatilityAvg, Double volumeRatioAvg) {
		this.timeframe = timeframe;
		this.count = count;
		this.volatilityAvg = volatilityAvg;
		this.volumeRatioAvg = volumeRatioAvg;
		this.createdAt = LocalDateTime.now();
	}

	public static DetectionStatistics of(Timeframe timeframe, Long count, Double volatilityAvg, Double volumeRatioAvg) {
		return DetectionStatistics.builder()
			.timeframe(timeframe)
			.count(count)
			.volatilityAvg(volatilityAvg)
			.volumeRatioAvg(volumeRatioAvg)
			.build();
	}
}