package com.coinsensor.common.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;

@Getter
public class PageResponse<T> {

	private final List<T> content;
	private final int pageNumber;
	private final int pageSize;
	private final long totalElements;
	private final int totalPages;
	private final boolean first;
	private final boolean last;

	private PageResponse(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages,
		boolean first, boolean last) {
		this.content = content;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.first = first;
		this.last = last;
	}

	public static <T> PageResponse<T> of(Page<T> page) {
		return new PageResponse<>(
			page.getContent(),
			page.getNumber(),
			page.getSize(),
			page.getTotalElements(),
			page.getTotalPages(),
			page.isFirst(),
			page.isLast()
		);
	}
}