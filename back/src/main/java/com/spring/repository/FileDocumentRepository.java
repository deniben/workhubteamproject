package com.spring.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.spring.entity.FileDocument;

@Repository
public interface FileDocumentRepository extends PagingAndSortingRepository<FileDocument, String> {
	Page<FileDocument> findByProjectId(Long projectId, Pageable pageable);
	FileDocument findByFileName(String fileName);
}
