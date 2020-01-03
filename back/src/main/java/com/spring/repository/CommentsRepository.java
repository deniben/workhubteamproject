package com.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.spring.entity.Comment;

@Repository
public interface CommentsRepository extends PagingAndSortingRepository<Comment, String> {
	Page<Comment> findByProjectId(Long id, Pageable pageable);
}
