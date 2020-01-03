package com.spring.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDto {

	private String id;

	private String text;

	private ProfileDto author;

	private LocalDateTime dateTime;

	List<CommentDto> replies = new ArrayList<>();

	public CommentDto() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ProfileDto getAuthor() {
		return author;
	}

	public void setAuthor(ProfileDto author) {
		this.author = author;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public List<CommentDto> getReplies() {
		return replies;
	}

	public void setReplies(List<CommentDto> replies) {
		this.replies = replies;
	}
}
