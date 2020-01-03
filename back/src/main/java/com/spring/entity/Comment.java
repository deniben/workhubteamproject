package com.spring.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
public class Comment {

	@Id
	private String id;

	private Long userId;

	private Long projectId;

	private String text;

	private LocalDateTime dateTime;

	private List<Comment> replies = new ArrayList<>();

	public Comment() {}

	public Comment(String id, Long userId, Long projectId, String text, LocalDateTime dateTime, List<Comment> replies) {
		this.id = id;
		this.userId = userId;
		this.projectId = projectId;
		this.text = text;
		this.dateTime = dateTime;
		this.replies = replies;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public List<Comment> getReplies() {
		return replies;
	}

	public void setReplies(List<Comment> replies) {
		this.replies = replies;
	}
}
