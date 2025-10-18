package com.coinsensor.economicevent.service;

import com.coinsensor.economicevent.dto.response.EconomicEventResponse;
import com.coinsensor.economicevent.repository.EconomicEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EconomicEventServiceImpl implements EconomicEventService {
    
    private final EconomicEventRepository economicEventRepository;
    
    @Override
    public List<EconomicEventResponse> getUpcomingEvents() {
        return economicEventRepository.findAll().stream()
                .filter(event -> event.getEventTime().isAfter(LocalDateTime.now()))
                .map(EconomicEventResponse::from)
                .toList();
    }
}
