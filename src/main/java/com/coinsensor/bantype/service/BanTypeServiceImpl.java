package com.coinsensor.bantype.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.bantype.dto.request.BanTypeRequest;
import com.coinsensor.bantype.dto.response.BanTypeResponse;
import com.coinsensor.bantype.entity.BanType;
import com.coinsensor.bantype.repository.BanTypeRepository;
import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BanTypeServiceImpl implements BanTypeService {

	private final BanTypeRepository banTypeRepository;

	@Override
	public BanTypeResponse createBanType(BanTypeRequest request) {
		BanType savedBanType = banTypeRepository.save(BanType.to(request));
		return BanTypeResponse.from(savedBanType);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BanTypeResponse> getAllBanTypes() {
		return banTypeRepository.findAll()
			.stream()
			.map(BanTypeResponse::from)
			.toList();
	}

	@Override
	public BanTypeResponse updateBanType(Long banTypeId, BanTypeRequest request) {
		BanType banType = banTypeRepository.findByBanTypeId(banTypeId)
			.orElseThrow(() -> new CustomException(ErrorCode.BAN_TYPE_NOT_FOUND));
		banType.update(request);
		return BanTypeResponse.from(banType);
	}

	@Override
	public void deleteBanType(Long banTypeId) {
		banTypeRepository.deleteByBanTypeId(banTypeId);
	}
}