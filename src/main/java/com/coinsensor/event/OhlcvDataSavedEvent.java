package com.coinsensor.event;

import com.coinsensor.exchange.entity.Exchange;

import lombok.Getter;

@Getter
public class OhlcvDataSavedEvent {
	private final String timeframeName;
	private final Exchange.Type exchangeType;
	private final int dataCount;

	public OhlcvDataSavedEvent(String timeframeName, Exchange.Type exchangeType, int dataCount) {
		this.timeframeName = timeframeName;
		this.exchangeType = exchangeType;
		this.dataCount = dataCount;
	}
}