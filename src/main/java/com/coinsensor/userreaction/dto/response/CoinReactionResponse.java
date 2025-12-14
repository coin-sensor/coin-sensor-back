package com.coinsensor.userreaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoinReactionResponse {
	private final String coinTicker;
	private final String baseAsset;
	private final Long reactionCount;
}