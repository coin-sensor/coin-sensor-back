package com.coinsensor.economicevent.controller;

import com.coinsensor.economicevent.dto.response.EconomicEventResponse;
import com.coinsensor.economicevent.service.EconomicEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EconomicEventController {
    
    private final EconomicEventService economicEventService;
    
    @GetMapping
    public ResponseEntity<List<EconomicEventResponse>> getUpcomingEvents() {
        return ResponseEntity.ok(economicEventService.getUpcomingEvents());
    }
}
