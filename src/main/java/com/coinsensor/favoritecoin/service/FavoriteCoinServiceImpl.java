package com.coinsensor.favoritecoin.service;

import static com.coinsensor.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import com.coinsensor.favoritecoin.dto.response.FavoriteCoinResponse;
import com.coinsensor.favoritecoin.entity.FavoriteCoin;
import com.coinsensor.favoritecoin.repository.FavoriteCoinRepository;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteCoinServiceImpl implements FavoriteCoinService {

	private final FavoriteCoinRepository favoriteCoinRepository;
	private final UserRepository userRepository;
	private final ExchangeCoinRepository exchangeCoinRepository;

	@Override
	public List<FavoriteCoinResponse> getFavoriteCoins(String uuid) {
		return favoriteCoinRepository.findAllByUuid(uuid);
	}

	@Override
	public void createOrDeleteFavoriteCoin(String uuid, Long exchangeCoinId) {
		if (favoriteCoinRepository.existsByUuidAndExchangeCoinId(uuid, exchangeCoinId)) {
			favoriteCoinRepository.deleteByUuidAndExchangeCoinId(uuid, exchangeCoinId);
		} else {
			User user = userRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
			ExchangeCoin exchangeCoin = exchangeCoinRepository.findById(exchangeCoinId)
				.orElseThrow(() -> new CustomException(EXCHANGE_COIN_NOT_FOUND));

			favoriteCoinRepository.save(FavoriteCoin.to(user, exchangeCoin));
		}

	}

}