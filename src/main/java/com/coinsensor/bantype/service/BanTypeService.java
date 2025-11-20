package com.coinsensor.bantype.service;

import com.coinsensor.bantype.dto.request.BanTypeRequest;
import com.coinsensor.bantype.dto.response.BanTypeResponse;
import java.util.List;

public interface BanTypeService {

    BanTypeResponse createBanType(BanTypeRequest request);

    List<BanTypeResponse> getAllBanTypes();

    void deleteBanType(Long banTypeId);
}