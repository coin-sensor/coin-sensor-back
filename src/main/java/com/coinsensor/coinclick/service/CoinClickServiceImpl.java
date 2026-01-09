package com.coinsensor.coinclick.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
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
	@Cacheable(value = "topViewedCoins", key = "#days + '_' + #limit")
	public List<CoinViewCountResponse> getTopViewedCoins(int days, int limit) {
		return coinClickRepository.findTopViewedCoins(days, limit);
	}

	@Override
	@Cacheable(value = "coinsTrendData", key = "#days + '_' + #limit")
	public List<CoinTrendDataResponse> getCoinsTrendData(int days, int limit) {
		return coinClickRepository.findCoinsTrendData(days, limit);
	}

}