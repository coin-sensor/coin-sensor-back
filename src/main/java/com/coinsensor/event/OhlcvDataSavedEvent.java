package com.coinsensor.event;

import com.coinsensor.exchange.entity.Exchange;

import lombok.Getter;

@Getter
public class OhlcvDataSavedEvent {
	private final String timeframeName;
	private final String exchangeName;
	private final Exchange.Type exchangeType;
	private final int dataCount;

	public OhlcvDataSavedEvent(String timeframeName, String exchangeName, Exchange.Type exchangeType, int dataCount) {
		this.timeframeName = timeframeName;
		this.exchangeName = exchangeName;
		this.exchangeType = exchangeType;
		this.dataCount = dataCount;
	}
}