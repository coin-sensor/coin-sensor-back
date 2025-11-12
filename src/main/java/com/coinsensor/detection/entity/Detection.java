package com.coinsensor.detection.entity;

import java.time.LocalDateTime;

import com.coinsensor.detectioncriteria.entity.DetectionCriteria;
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
	@JoinColumn(name = "detection_criteria_id", nullable = false)
	private DetectionCriteria detectionCriteria;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "exchange_id", nullable = false)
	private Exchange exchange;

	@Column(columnDefinition = "TEXT", nullable = false)
	private String summary;

	@Column(name = "detection_count", nullable = false)
	private Long detectionCount;

	@Column(name = "detected_at", nullable = false)
	private LocalDateTime detectedAt;

	public Detection(DetectionCriteria detectionCriteria, Exchange exchange, String summary, Long detectionCount) {
		this.detectionCriteria = detectionCriteria;
		this.exchange = exchange;
		this.summary = summary;
		this.detectionCount = detectionCount;
		this.detectedAt = LocalDateTime.now();
	}

	public static Detection to(DetectionCriteria detectionCriteria, Exchange exchange, String summary,
		Long detectionCount) {
		return Detection.builder()
			.detectionCriteria(detectionCriteria)
			.exchange(exchange)
			.summary(summary)
			.detectionCount(detectionCount)
			.detectedAt(LocalDateTime.now())
			.build();
	}
}
