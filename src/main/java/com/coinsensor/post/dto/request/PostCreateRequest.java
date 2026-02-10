package com.coinsensor.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

	private Long categoryId;
	private String title;
	private String content;
}