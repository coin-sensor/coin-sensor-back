package com.coinsensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
})
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.coinsensor")
public class CoinSensorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinSensorApplication.class, args);
	}

}
