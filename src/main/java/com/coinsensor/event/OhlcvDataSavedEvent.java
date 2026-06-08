package com.coinsensor.event;

import java.time.LocalDateTime;

import com.coinsensor.exchange.entity.Exchange;

import lombok.Getter;

@Getter
public class OhlcvDataSavedEvent {
	private final String timeframeName;
	private final String exchangeName;
	private final Exchange.Type exchangeType;
	private final int dataCount;
	private final LocalDateTime candleStartTime;

	public OhlcvDataSavedEvent(String timeframeName, String exchangeName, Exchange.Type exchangeType, int dataCount,
		LocalDateTime candleStartTime) {
		this.timeframeName = timeframeName;
		this.exchangeName = exchangeName;
		this.exchangeType = exchangeType;
		this.dataCount = dataCount;
		this.candleStartTime = candleStartTime;
	}
}