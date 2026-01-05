package com.coinsensor.detection.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopDetectedCoinResponse {
	private String symbol;
	private Long count;
	private BigDecimal maxChangeX;
	private BigDecimal maxVolumeX;
}