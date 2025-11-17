package com.coinsensor.detectedcoin.repository;

import java.util.List;

import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.timeframe.entity.Timeframe;

public interface CustomDetectedCoinRepository {

	List<DetectedCoin> finAllByTimeframe(Timeframe timeframe);
}
