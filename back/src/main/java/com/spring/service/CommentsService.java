package com.spring.service;

import org.springframework.data.domain.Page;

import com.spring.entity.Comment;

public interface CommentsService {

	Page<Comment> getByProject(Long id, Integer page);

	void addComment(Comment comment);

	void replyComment(Comment comment, String replyId);

	void deleteCommentById(String id);
}
