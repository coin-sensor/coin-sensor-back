package com.coinsensor.common.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DataSourceConfig {

	/**
	 * PoolSize = Tn × ( Cm - 1 ) + ( Tn / 2 )
	 * Tn: thread count (CPU cores)
	 * Cm: simultaneous connection count (2)
	 */
	private final int poolSize = calculatePoolSize();

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	private int calculatePoolSize() {
		int threadCount = Runtime.getRuntime().availableProcessors();
		int simultaneousConnectionCount = 2;
		int calculatedPoolSize = threadCount * (simultaneousConnectionCount - 1) + (threadCount / 2);

		log.info("[DataSource Pool Size] - CPU Cores: {}, Pool Size: {}", threadCount, calculatedPoolSize);
		return calculatedPoolSize;
	}

	@Bean
	@Profile("dev")
	public DataSource devDataSource(@Value("${spring.datasource.url}") String url) {
		return createDataSource(url);
	}

	@Bean
	@Profile("prod")
	public DataSource prodDataSource(@Value("${spring.datasource.url}") String url) {
		return createDataSource(url);
	}

	// @Bean
	// @Profile("prod")
	// public DataSource routingDataSource(
	// 	@Qualifier("writeDataSource") DataSource writeDataSource,
	// 	@Qualifier("readDataSource") DataSource readDataSource) {
	// 	RoutingDataSource routingDataSource = new RoutingDataSource();
	// 	Map<Object, Object> dataSourceMap = new HashMap<>();
	// 	dataSourceMap.put(DataSourceContextHolder.WRITE, writeDataSource);
	// 	dataSourceMap.put(DataSourceContextHolder.READ, readDataSource);
	//
	// 	routingDataSource.setTargetDataSources(dataSourceMap);
	// 	routingDataSource.setDefaultTargetDataSource(writeDataSource);
	//
	// 	return routingDataSource;
	// }
	//
	// @Primary
	// @Bean
	// @Profile("prod")
	// public DataSource dataSource(DataSource routingDataSource) {
	// 	return new LazyConnectionDataSourceProxy(routingDataSource);
	// }
	//
	// @Bean
	// @Profile("prod")
	// public DataSource writeDataSource(@Value("${spring.datasource.router.url.rw}") String url) {
	// 	return createDataSource(url);
	// }
	//
	// @Bean
	// @Profile("prod")
	// public DataSource readDataSource(@Value("${spring.datasource.router.url.ro}") String url) {
	// 	return createDataSource(url);
	// }

	private DataSource createDataSource(String url) {
		HikariDataSource dataSource = DataSourceBuilder.create()
			.type(HikariDataSource.class)
			.url(url)
			.username(username)
			.password(password)
			.driverClassName("com.mysql.cj.jdbc.Driver")
			.build();

		// 💡 CPU 환경에 따른 동적 풀 사이즈 설정
		dataSource.setMinimumIdle(poolSize / 2); // 최대값의 50%
		dataSource.setMaximumPoolSize(poolSize);
		dataSource.setConnectionTestQuery("SELECT 1");
		dataSource.setKeepaliveTime(30000);  // 30초마다 Keepalive 쿼리 실행 (DBCP에는 없는 기능)
		dataSource.setIdleTimeout(60000);    // 60초 이상 Idle이면 제거 (minEvictableIdleTimeMillis 대체)
		dataSource.setMaxLifetime(1800000);  // 30분(1800초) 후 커넥션 새로 생성 (DB 세션 유지 문제 방지)

		return dataSource;
	}
}