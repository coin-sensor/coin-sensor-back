package com.coinsensor.coinclick.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.coinclick.dto.response.CoinTrendDataResponse;
import com.coinsensor.coinclick.dto.response.CoinViewCountResponse;
import com.coinsensor.coinclick.repository.CoinClickRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CoinClickServiceImpl implements CoinClickService {

	private final CoinClickRepository coinClickRepository;

	@Override
	public List<CoinViewCountResponse> getTopViewedCoins(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return coinClickRepository.findTopViewedCoins(startTime, limit);
	}

	@Override
	public List<CoinTrendDataResponse> getCoinsTrendData(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return coinClickRepository.findCoinsTrendData(startTime, limit);
	}

}