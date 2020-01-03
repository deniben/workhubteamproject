package com.spring.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.spring.entity.FileDocument;

public interface WorkspaceService {
	void shareDocument(MultipartFile file, Long projectId) throws IOException;
	Page<FileDocument> getProjectDocuments(Long projectId, Long page);
	void deleteDocument(String fileName) throws IOException;
	byte [] download(String fileName);
	void init() throws IOException;
}
