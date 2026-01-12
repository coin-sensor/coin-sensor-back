package com.coinsensor.ohlcvs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.annotation.AuthorizeRole;
import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.ohlcvs.service.OhlcvService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ohlcvs")
@RequiredArgsConstructor
public class OhlcvController {

	private final OhlcvService ohlcvService;

	@AuthorizeRole
	@DeleteMapping("/cleanup")
	public ResponseEntity<ApiResponse<String>> cleanupOldData(@RequestParam(defaultValue = "1") int years) {
		long deletedCount = ohlcvService.cleanupOldData(years);
		return ApiResponse.createSuccess(String.format("%d년 이상 된 OHLCV 데이터 %d건이 삭제되었습니다.", years, deletedCount));
	}
}