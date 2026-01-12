package com.coinsensor.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.coinsensor.common.dto.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(CustomException e) {
		return ApiResponse.createError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorResponse.of(e.getErrorCode()).toString());
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleResponseStatusException(ResponseStatusException e) {
		return ApiResponse.createError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e,
		jakarta.servlet.http.HttpServletRequest request) {
		return ApiResponse.createError(HttpStatus.METHOD_NOT_ALLOWED,
			String.format("지원하지 않는 API: %s %s", e.getMethod(), request.getRequestURI()));
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException e) {
		return ApiResponse.createError(HttpStatus.NOT_FOUND, "NoResourceFoundException");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<ErrorResponse>> handleException(Exception e) {
		log.error("Exception: ", e);
		return ApiResponse.createError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}
}
