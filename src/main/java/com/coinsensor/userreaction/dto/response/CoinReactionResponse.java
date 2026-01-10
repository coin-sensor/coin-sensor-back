package com.coinsensor.userreaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoinReactionResponse {
	private String coinTicker;
	private String baseAsset;
	private Long reactionCount;
}