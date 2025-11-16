package com.coinsensor.coin.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.coin.dto.response.CoinResponse;
import com.coinsensor.coin.repository.CoinRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoinServiceImpl implements CoinService {

	private final CoinRepository coinRepository;

	@Override
	public List<CoinResponse> getAllCoins() {
		return coinRepository.findAll().stream()
			.map(CoinResponse::from)
			.toList();
	}

	@Override
	public CoinResponse getCoinById(Long coinId) {
		return coinRepository.findById(coinId)
			.map(CoinResponse::from)
			.orElseThrow(() -> new RuntimeException("Coin not found"));
	}

}
