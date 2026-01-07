package com.coinsensor.ohlcvs.service;

import java.util.List;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.websocket.dto.KlineData;

public interface OhlcvService {

	void saveKlineData(List<KlineData> klineDataList, Exchange.Type exchangeType);

	long cleanupOldData(int years);
}