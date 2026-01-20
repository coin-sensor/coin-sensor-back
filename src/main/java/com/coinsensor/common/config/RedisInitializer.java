package com.coinsensor.common.config;

import java.util.Objects;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.coinsensor.common.annotation.LeaderOnly;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisInitializer {
	private final RedisTemplate<String, Object> redisTemplate;

	@LeaderOnly
	@PostConstruct
	public void clearRedisOnStartup() {
		Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushAll();
		log.info("Redis 초기화");
	}
}