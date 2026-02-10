package com.coinsensor.postview.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.coinsensor.post.entity.Post;
import com.coinsensor.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_views")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostView {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_view_id")
	private Long postViewId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "view_count", nullable = false)
	private Long viewCount = 0L;

	public static PostView to(Post post, User user) {
		PostView postView = new PostView();
		postView.post = post;
		postView.user = user;
		postView.viewCount = 0L;
		return postView;
	}

	public void increaseViewCount() {
		this.viewCount++;
	}
}