package com.coinsensor.economicevent.service;

import com.coinsensor.economicevent.dto.response.EconomicEventResponse;
import java.util.List;

public interface EconomicEventService {
    List<EconomicEventResponse> getUpcomingEvents();
}
