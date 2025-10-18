package com.coinsensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoinSensorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinSensorApplication.class, args);
	}

}
