package com.coinsensor.bantype.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.bantype.dto.request.BanTypeRequest;
import com.coinsensor.bantype.dto.response.BanTypeResponse;
import com.coinsensor.bantype.service.BanTypeService;
import com.coinsensor.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/banTypes")
@RequiredArgsConstructor
public class BanTypeController {

	private final BanTypeService banTypeService;

	@PostMapping
	public ResponseEntity<ApiResponse<BanTypeResponse>> createBanType(@RequestBody BanTypeRequest request) {
		BanTypeResponse response = banTypeService.createBanType(request);
		return ApiResponse.createSuccess(response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<BanTypeResponse>>> getAllBanTypes() {
		List<BanTypeResponse> responses = banTypeService.getAllBanTypes();
		return ApiResponse.createSuccess(responses);
	}

	@PutMapping("/{banTypeId}")
	public ResponseEntity<ApiResponse<BanTypeResponse>> updateBanType(@PathVariable Long banTypeId,
		@RequestBody BanTypeRequest request) {
		BanTypeResponse response = banTypeService.updateBanType(banTypeId, request);
		return ApiResponse.createSuccess(response);
	}

	@DeleteMapping("/{banTypeId}")
	public ResponseEntity<ApiResponse<Void>> deleteBanType(@PathVariable Long banTypeId) {
		banTypeService.deleteBanType(banTypeId);
		return ResponseEntity.ok().build();
	}
}