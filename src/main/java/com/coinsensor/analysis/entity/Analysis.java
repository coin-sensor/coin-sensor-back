package com.coinsensor.analysis.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.coinsensor.coin.entity.Coin;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "analyses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Analysis {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ai_analysis_id")
	private Long aiAnalysisId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "coin_id", nullable = false)
	private Coin coin;

	@Column(name = "report_time", nullable = false)
	private LocalDateTime reportTime;

	@Column(name = "request", nullable = false, columnDefinition = "TEXT")
	private String request;

	@Column(name = "response", nullable = false, columnDefinition = "TEXT")
	private String response;

	@Column(name = "summary_text", columnDefinition = "TEXT")
	private String summaryText;

	@Enumerated(EnumType.STRING)
	@Column(name = "trend_prediction")
	private TrendPrediction trendPrediction = TrendPrediction.neutral;

	@Column(name = "is_volatility_alert")
	private Boolean isVolatilityAlert = false;

	@Column(name = "recommendation", columnDefinition = "TEXT")
	private String recommendation;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "view_count", nullable = false)
	private Long viewCount = 0L;

	public static Analysis to(Coin coin, LocalDateTime reportTime, String request, String response) {
		Analysis analysis = new Analysis();
		analysis.coin = coin;
		analysis.reportTime = reportTime;
		analysis.request = request;
		analysis.response = response;
		return analysis;
	}

	public void incrementViewCount() {
		this.viewCount++;
	}

	public enum TrendPrediction {
		bullish, bearish, neutral
	}
}