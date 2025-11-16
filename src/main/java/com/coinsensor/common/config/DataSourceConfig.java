package com.coinsensor.common.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
	@Value("${spring.datasource.dbcp2.username}")
	private String username;
	@Value("${spring.datasource.dbcp2.password}")
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

	private BasicDataSource createDataSource(String url) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");

		// 💡 CPU 환경에 따른 동적 풀 사이즈 설정
		dataSource.setInitialSize(poolSize);
		dataSource.setMaxTotal(poolSize);
		dataSource.setMaxIdle(poolSize);
		dataSource.setMinIdle(poolSize);

		dataSource.setTestOnBorrow(true);
		dataSource.setValidationQuery("SELECT 1");

		return dataSource;
	}
}