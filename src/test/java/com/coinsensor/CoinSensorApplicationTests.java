package com.coinsensor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.coinsensor.detectionstatistics.service.DetectionStatisticsService;

@SpringBootTest
@ActiveProfiles("test")
class CoinSensorApplicationTests {

	@Autowired
	private DetectionStatisticsService detectionStatisticsService;

	@Test
	void testAggregateDetectionStatistics() {
		detectionStatisticsService.aggregateDetectionStatistics();
	}

}
