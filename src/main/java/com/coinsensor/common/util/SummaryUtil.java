package com.coinsensor.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectioncriteria.entity.DetectionCriteria;

public class SummaryUtil {

	public static String create(DetectionCriteria detectionCriteria,  List<DetectedCoin> detectedCoins) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(EEE) HH시 mm분 ss초", Locale.KOREAN);
		String timestamp = now.format(formatter);

		StringBuilder summary = new StringBuilder();
		summary.append(String.format("🚨 %s 🚨\n", timestamp));
		summary.append(String.format("거래소: [Binance-Future]\n"));
		summary.append(String.format("기준 : %s, 기준 변동률 : %.2f%%, 기준 배수 : %.1f배\n\n",
			detectionCriteria.getTimeframe().getTimeframeLabel(),
			detectionCriteria.getVolatility(),
			detectionCriteria.getVolume()));

		for (DetectedCoin detected : detectedCoins) {
			summary.append(String.format("종목 : %s\n변동률 : %5.2f%%,  거래량 : %5.1f배\n\n",
				detected.getCoin().getCoinTicker(),
				detected.getVolatility(),
				detected.getVolume()));
		}

		return summary.toString();
	}
}
