package com.coinsensor.telegram.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.coinsensor.detection.entity.Detection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramService {

	private final RestTemplate restTemplate;
	@Value("${telegram.bot.token}")
	private String botToken;
	@Value("${telegram.chat.id}")
	private String chatId;
	@Value("${spring.profiles.active:dev}")
	private String activeProfile;

	public void sendTelegramNotification(Detection detection, String coinCategory, String message) {
		// 배포환경에서만 작동
		if (!"prod".equals(activeProfile)) {
			return;
		}

		String exchangeName = detection.getExchange().getName();
		String exchangeType = detection.getExchange().getType().name();
		String timeframe = detection.getCondition().getTimeframe().getName();

		if (!exchangeName.equals("binance") || !exchangeType.equals("future")) {
			return;
		}

		boolean shouldSend = switch (timeframe) {
			case "1m", "5m", "15m" -> coinCategory.equals("top20");
			case "1h", "4h", "1d" -> coinCategory.equals("all");
			default -> false;
		};

		if (shouldSend) {
			sendMessage(timeframe, message);
		}
	}

	public void sendMessage(String timeframe, String text) {
		try {
			String url = String.format(
				"https://api.telegram.org/bot%s/sendMessage?chat_id=%s&message_thread_id=%s&text=%s",
				botToken, chatId, selectThreadId(timeframe), text);

			restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			log.error("텔레그램 메시지 전송 실패: {}", e.getMessage());
		}
	}

	public String selectThreadId(String timeframe) {
		return switch (timeframe) {
			case "1m" -> "2";
			case "5m" -> "4";
			case "15m" -> "5";
			case "1h" -> "6";
			case "4h" -> "22";
			case "1d" -> "24";
			default -> "null";
		};
	}
}