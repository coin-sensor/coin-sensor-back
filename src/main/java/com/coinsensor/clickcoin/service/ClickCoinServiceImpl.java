package com.coinsensor.clickcoin.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.clickcoin.dto.response.CoinTrendDataResponse;
import com.coinsensor.clickcoin.dto.response.CoinViewCountResponse;
import com.coinsensor.clickcoin.repository.ClickCoinRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClickCoinServiceImpl implements ClickCoinService {

	private final ClickCoinRepository clickCoinRepository;

	@Override
	public List<CoinViewCountResponse> getTopViewedCoins(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return clickCoinRepository.findTopViewedCoins(startTime, limit);
	}

	@Override
	public List<CoinTrendDataResponse> getCoinsTrendData(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return clickCoinRepository.findCoinsTrendData(startTime, limit);
	}

}