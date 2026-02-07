package com.coinsensor.user.filter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.dao.DataIntegrityViolationException;

import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserFilter implements Filter {
	private final UserRepository userRepository;
	private final ConcurrentHashMap<String, Object> lockMap = new ConcurrentHashMap<>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest)request;

		// /actuator 경로는 필터 제외
		if (httpRequest.getRequestURI().startsWith("/api/actuator")) {
			chain.doFilter(request, response);
			return;
		}

		// OPTIONS 요청(CORS preflight)은 UUID 검증 건너뛰기
		if ("OPTIONS".equals(httpRequest.getMethod())) {
			chain.doFilter(request, response);
			return;
		}

		String uuid = getUuidFromRequest(httpRequest);

		if (uuid != null) {
			// 동시성 문제를 해결
			Object lock = lockMap.computeIfAbsent(uuid, k -> new Object());
			synchronized (lock) {
				try {
					userRepository.findByUuid(uuid).orElseGet(() ->
						userRepository.save(User.to(uuid, getClientIpAddress(httpRequest))));
				} catch (DataIntegrityViolationException e) {
					log.debug("User with UUID {} already exists, skipping creation", uuid);
				}
			}
			lockMap.remove(uuid);
		}

		chain.doFilter(request, response);
	}

	private String getUuidFromRequest(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		String fullUrl = queryString != null ? url + "?" + queryString : url;
		String uuid;
		// 웹소켓 핸드셰이크는 UUID 검증 건너뛰기
		if (isWebSocketRequest(request)) {
			uuid = request.getParameter("uuid");
			log.info("[WebSocket] URL: {}, UUID: {}", fullUrl, uuid);
		} else {
			// 일반 HTTP 요청은 헤더에서 UUID 확인
			uuid = request.getHeader("uuid");
			log.info("[HTTP] URL: {}, UUID: {}", fullUrl, uuid);
		}

		return uuid;
	}

	private boolean isWebSocketRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return uri.startsWith("/ws");
	}

	private String getClientIpAddress(HttpServletRequest request) {
		String xForwardedFor = request.getHeader("X-Forwarded-For");
		if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
			return xForwardedFor.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}
}