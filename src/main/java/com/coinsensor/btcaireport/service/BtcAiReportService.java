package com.coinsensor.btcaireport.service;

import com.coinsensor.btcaireport.dto.response.BtcAiReportResponse;

public interface BtcAiReportService {
    BtcAiReportResponse getLatestReport();
}
