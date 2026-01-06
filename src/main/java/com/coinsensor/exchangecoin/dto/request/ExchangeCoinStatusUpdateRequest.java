package com.coinsensor.exchangecoin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeCoinStatusUpdateRequest {

	private Boolean enableDetection;
}